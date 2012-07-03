package fr.ultimate.breakfast.web.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;

/**
 * Start page of application ultimate-breakfast-web.
 */
public class Index
{
    @Property
    @SuppressWarnings("unused")
    private boolean loginFailed;

    @Property
    @SuppressWarnings("unused")
    private String contextRoot;

    @Inject
    private RequestGlobals globals;

    @InjectPage
    private TimeToEat timeToEat;

    @Inject
    private UltimateBreakfastSecurityContext securityContext;

    Object onActivate()
    {
        // If the user is logged in, redirect to timeToEat page
        boolean isLoggedIn = securityContext.isLoggedIn();
        if (isLoggedIn) { return timeToEat; }
        return null;
    }

    void onActivate(@RequestParameter(value = "loginFailed", allowBlank = true)
    boolean loginFailed)
    {
        this.loginFailed = loginFailed;
        this.contextRoot = globals.getRequest().getContextPath();
    }

}
