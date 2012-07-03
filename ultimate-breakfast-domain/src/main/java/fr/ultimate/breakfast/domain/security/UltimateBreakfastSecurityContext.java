/**
 * 
 */
package fr.ultimate.breakfast.domain.security;

import fr.ultimate.breakfast.domain.model.Team;

/**
 * Contexte de sécurité de l'application UltimateBreakfast.
 * 
 * @author lguerin
 */
public interface UltimateBreakfastSecurityContext
{
    /**
     * Authenticate a user.
     */
    void login(String login, String password);

    /**
     * Teste si un utilisateur est authentifié ou non.
     * 
     * @return true si authentifié
     */
    boolean isLoggedIn();

    /**
     * Retourne l'utilisateur {@link Team} authentifié.
     * 
     * @return utilisateur courant authentifié.
     */
    Team getTeam();

    /**
     * Deconnecte l'utilisateur courant
     */
    void logout();
}
