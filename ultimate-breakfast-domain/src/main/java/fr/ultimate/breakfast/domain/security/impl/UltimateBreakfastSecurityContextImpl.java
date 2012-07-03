/**
 * 
 */
package fr.ultimate.breakfast.domain.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;

/**
 * Implementation du SecurityContext propre à ultimate-breakfast
 * 
 * @author lguerin
 */
@Component("ultimate-breakfastSecurityContext")
public class UltimateBreakfastSecurityContextImpl implements UltimateBreakfastSecurityContext
{
    /**
     * UltimateBreakfast {@link BreakfastManager}
     */
    @Autowired
    private BreakfastManager breakfastManager;

    @Autowired
    private SaltSource saltSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     * (non-Javadoc)
     * @see
     * fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext#login(fr.ultimate.
     * breakfast.domain.model
     * .User)
     */
    @Override
    public void login(String login, String password)
    {
        Assert.notNull(login, "login");
        Assert.notNull(password, "password");

        Team exist = breakfastManager.findByName(login);
        if (exist != null)
        {
            String toCheck = passwordEncoder.encodePassword(password, saltSource.getSalt(exist));
            if (exist.getPassword().equals(toCheck))
            {
                UsernamePasswordAuthenticationToken logged = new UsernamePasswordAuthenticationToken(exist, exist.getPassword(), exist.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(logged);
                return;
            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /*
     * (non-Javadoc)
     * @see fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext#isLoggedIn()
     */
    @Override
    public boolean isLoggedIn()
    {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null)
        {
            if ("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())) { return false; }
            return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext#getUser()
     */
    @Override
    public Team getTeam()
    {
        Team team = null;
        if (isLoggedIn())
        {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Team)
            {
                team = ((Team) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }
        }
        return team;
    }

    /*
     * (non-Javadoc)
     * @see fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext#logout()
     */
    @Override
    public void logout()
    {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

}
