/**
 * 
 */
package fr.ultimate.breakfast.domain.security.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Team;

/**
 * Implémentation de l'interface {@link UserDetailsService} fournie par Spring Security.
 * Permet de faire le pont entre notre implémentation spécifique et le gestionnaire
 * d'authentification de Spring Security.
 * 
 * @author lguerin
 */
@Component("ultimate-breakfastUserDetailService")
public class UserDetailsServiceImpl implements UserDetailsService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    /**
     * UltimateBreakfast {@link BreakfastManager}
     */
    @Autowired
    private BreakfastManager breakfastManager;

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.
     * lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(">>> loadUserByUsername username: " + username);
        }
        Team team = breakfastManager.findByName(username);

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("<<< User: " + team);
        }
        return team;
    }

}
