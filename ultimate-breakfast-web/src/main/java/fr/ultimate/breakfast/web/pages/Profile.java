/**
 * 
 */
package fr.ultimate.breakfast.web.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;

/**
 * User Profile page
 * 
 * @author lguerin
 */
public class Profile
{
    @Property
    private Team team;

    @Persist
    @Property
    private String name;

    @Persist
    @Property
    private String email;

    @Property
    private String password;

    @Property
    private String verifyPassword;

    @Component
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private BreakfastManager breakfastManager;

    @Inject
    private UltimateBreakfastSecurityContext securityContext;

    @InjectPage
    private Index index;

    @OnEvent(value = EventConstants.ACTIVATE)
    void activate()
    {
        team = breakfastManager.find(securityContext.getTeam().getId());
        if (name == null)
        {
            name = team.getName();
        }
        if (email == null)
        {
            email = team.getEmail();
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "form")
    Object update()
    {
        if (verifyPassword != null || password != null)
        {
            if (!verifyPassword.equals(password))
            {
                form.recordError(messages.get("password-match-error"));
                return null;
            }
        }

        if (!securityContext.getTeam().getName().equals(name))
        {
            Team exist = breakfastManager.findByName(name);
            if (exist != null)
            {
                form.recordError(messages.get("team-name-error"));
                return null;
            }
        }

        team.setName(name);
        team.setEmail(email);

        // Update Team and credentials if needed
        securityContext.getTeam().setName(team.getName());
        breakfastManager.updateProfile(team, password);

        return index;
    }
}
