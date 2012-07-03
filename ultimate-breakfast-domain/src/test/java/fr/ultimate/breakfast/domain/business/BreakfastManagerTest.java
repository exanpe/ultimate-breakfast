/**
 * 
 */
package fr.ultimate.breakfast.domain.business;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.spring.annotation.SpringBeanByType;

import fr.ultimate.breakfast.domain.base.UltimateBreakfastDomainBaseTest;
import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;

/**
 * @author lguerin
 */
@DataSet("/dataset/UltimateBreakfastBusinessTest.xml")
public class BreakfastManagerTest extends UltimateBreakfastDomainBaseTest
{
    @SpringBeanByType
    private BreakfastManager breakfastManager;

    private static final String AGEFOS_PROJECT = "Agefos";

    @Test
    public void findByName()
    {
        Team expected = breakfastManager.findByName(AGEFOS_PROJECT);
        ReflectionAssert.assertPropertyReflectionEquals("id", 1L, expected);
        ReflectionAssert.assertPropertyReflectionEquals("name", "Agefos", expected);
        ReflectionAssert.assertPropertyReflectionEquals("password", "age.fos", expected);

        Assert.assertNotNull(expected.getEaters());
        Assert.assertFalse(expected.getEaters().isEmpty());
        Assert.assertEquals(expected.getEaters().size(), 3);
    }

    @Test
    public void findEatersByTeam()
    {
        Team expected = breakfastManager.findByName(AGEFOS_PROJECT);

        List<Eater> eaters = breakfastManager.findEatersByTeam(expected);

        Assert.assertNotNull(eaters);
        Assert.assertFalse(eaters.isEmpty());
        Assert.assertEquals(eaters.size(), 3);
    }

    @Test
    public void deleteEater()
    {
        Team expected = breakfastManager.findByName(AGEFOS_PROJECT);

        Assert.assertNotNull(expected.getEaters());
        Assert.assertFalse(expected.getEaters().isEmpty());
        Assert.assertEquals(expected.getEaters().size(), 3);

        breakfastManager.deleteEater(expected.getEaters().get(0).getId());

        List<Eater> eaters = breakfastManager.findEatersByTeam(expected);

        Assert.assertNotNull(eaters);
        Assert.assertFalse(eaters.isEmpty());
        Assert.assertEquals(eaters.size(), 2);

    }

    @Test
    public void addEater()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);

        Assert.assertNotNull(team.getEaters());
        Assert.assertFalse(team.getEaters().isEmpty());
        Assert.assertEquals(team.getEaters().size(), 3);

        Eater e = new Eater();
        e.setName("tquaryan");

        breakfastManager.addEater(team, e);

        List<Eater> eaters = breakfastManager.findEatersByTeam(team);

        Assert.assertNotNull(eaters);
        Assert.assertFalse(eaters.isEmpty());
        Assert.assertEquals(eaters.size(), 4);

    }

    @Test
    public void updateEater()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);

        Assert.assertNotNull(team.getEaters());
        Assert.assertFalse(team.getEaters().isEmpty());
        Assert.assertEquals(team.getEaters().size(), 3);

        Eater e = team.getEaters().get(0);
        e.setPosition(15);

        breakfastManager.updateEater(e);

        List<Eater> eaters = breakfastManager.findEatersByTeam(team);

        Assert.assertNotNull(eaters);
        Assert.assertFalse(eaters.isEmpty());
        Assert.assertEquals(eaters.size(), 3);
        Assert.assertEquals(eaters.get(0).getPosition().longValue(), 15);

    }

    @Test
    public void getNextMaxPosition()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);

        Assert.assertEquals(breakfastManager.getNextMaxPosition(team), 6);

        Eater e = team.getEaters().get(0);
        e.setPosition(15);

        breakfastManager.updateEater(e);

        Assert.assertEquals(breakfastManager.getNextMaxPosition(team), 16);
    }

    @Test
    public void incrementEaters()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);
        int positionJmx = 0;
        int positionEdo = 0;
        List<Eater> toIncrement = new ArrayList<Eater>();

        for (int i = 0; i < team.getEaters().size(); i++)
        {
            if (team.getEaters().get(i).getName().equals("jmaupoux"))
            {
                positionJmx = team.getEaters().get(i).getPosition();
                toIncrement.add(team.getEaters().get(i));
            }
            else if (team.getEaters().get(i).getName().equals("edournes"))
            {
                positionEdo = team.getEaters().get(i).getPosition();
                toIncrement.add(team.getEaters().get(i));
            }
        }

        breakfastManager.incrementEaters(toIncrement);

        List<Eater> eaters = breakfastManager.findEatersByTeam(team);

        for (int i = 0; i < eaters.size(); i++)
        {
            if (eaters.get(i).getName().equals("jmaupoux"))
            {
                Assert.assertEquals(eaters.get(i).getPosition().intValue(), positionJmx + 1);
            }
            else if (eaters.get(i).getName().equals("edournes"))
            {
                Assert.assertEquals(eaters.get(i).getPosition().intValue(), positionEdo + 1);
            }
        }
    }

    @Test
    public void topEaters()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);
        List<Eater> toTop = new ArrayList<Eater>();

        for (int i = 0; i < team.getEaters().size(); i++)
        {
            if (team.getEaters().get(i).getName().equals("jmaupoux"))
            {
                toTop.add(team.getEaters().get(i));
            }
            else if (team.getEaters().get(i).getName().equals("edournes"))
            {
                toTop.add(team.getEaters().get(i));
            }
        }

        breakfastManager.topEaters(team, toTop);

        List<Eater> eaters = breakfastManager.findEatersByTeam(team);

        for (int i = 0; i < eaters.size(); i++)
        {
            if (eaters.get(i).getName().equals("jmaupoux"))
            {
                Assert.assertEquals(eaters.get(i).getPosition().intValue(), 6);
            }
            else if (eaters.get(i).getName().equals("edournes"))
            {
                Assert.assertEquals(eaters.get(i).getPosition().intValue(), 6);
            }
            else
            {
                Assert.assertNotSame(eaters.get(i).getPosition().intValue(), 6);
            }
        }
    }

    @Test
    public void findEaterByName()
    {
        Team team = breakfastManager.findByName(AGEFOS_PROJECT);
        Eater eater = breakfastManager.findEaterByName(team, "jmaupoux");
        Assert.assertNotNull(eater);
        Assert.assertEquals(eater.getPosition().intValue(), 3);
    }
}
