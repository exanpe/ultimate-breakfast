/**
 * 
 */
package fr.ultimate.breakfast.web.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;

/**
 * Sign Up page
 * 
 * @author lguerin
 */
public class SignUp
{
    @Property
    private String name;

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
    public Object activate()
    {
        return securityContext.isLoggedIn() ? Index.class : null;
    }

    @OnEvent(value = EventConstants.VALIDATE, component = "form")
    public void validate()
    {
        if (!verifyPassword.equals(password))
        {
            form.recordError(messages.get("password-match-error"));
        }
        Team exist = breakfastManager.findByName(name);
        if (exist != null)
        {
            form.recordError(messages.get("team-name-error"));
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "form")
    public Object register()
    {
        // Create new team
        Team team = new Team();
        team.setName(name);
        team.setEmail(email);
        team.setPassword(password);
        breakfastManager.create(team);

        // Authenticate new user
        securityContext.login(name, password);
        return index;
    }
}
