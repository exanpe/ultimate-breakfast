package fr.ultimate.breakfast.web.pages;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;

/**
 * Display a team planning
 * 
 * @author jmaupoux
 */
public class Planning
{
    @Property
    @Persist(PersistenceConstants.FLASH)
    private String team;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private List<Eater> eaters;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private Eater currentEater;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private int currentIndex;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private Date lastNotification;

    @Inject
    private BreakfastManager breakfastManager;

    @Inject
    private Block unknown;

    @Inject
    private Block results;

    @OnEvent(value = EventConstants.ACTION)
    Object search()
    {
        Team t = breakfastManager.findByName(team);

        if (t == null) { return unknown; }

        eaters = breakfastManager.findEatersByTeam(t);

        lastNotification = t.getLastNotification();

        return results;
    }

    /**
     * @return the currentIndex
     */
    public int getDisplayCurrentIndex()
    {
        return currentIndex + 1;
    }
}
