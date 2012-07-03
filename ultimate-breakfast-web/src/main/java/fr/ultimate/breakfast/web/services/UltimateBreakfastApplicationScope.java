/**
 * 
 */
package fr.ultimate.breakfast.web.services;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Team;

/**
 * @author jmaupoux
 */
public class UltimateBreakfastApplicationScope
{
    /**
     * Logger de la classe
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UltimateBreakfastApplicationScope.class);

    private String adminMail;

    /**
     * Constructeur.
     * 
     * @param context Contexte Spring
     * @param productionMode Mode Tapestry (PRODUCTION ou DEVELOPPEMENT)
     * @throws SQLException
     */
    public UltimateBreakfastApplicationScope(ApplicationContext context) throws SQLException
    {
        adminMail = initAdminMail(context);

    }

    private String initAdminMail(ApplicationContext context)
    {
        BreakfastManager breakfastManager = (BreakfastManager) context.getBean(BreakfastManager.class);
        Team p = breakfastManager.findByName("admin");
        if (p == null)
            return "";
        return p.getEmail();
    }

    public String getAdminMail()
    {
        return adminMail;
    }
}
