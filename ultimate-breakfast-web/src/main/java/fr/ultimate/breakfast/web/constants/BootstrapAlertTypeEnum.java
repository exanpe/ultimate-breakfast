/**
 * 
 */
package fr.ultimate.breakfast.web.constants;

import fr.ultimate.breakfast.web.components.BootstrapAlert;

/**
 * Type d'alertes prise en compte par le composant {@link BootstrapAlert}
 * 
 * @see http://twitter.github.com/bootstrap/components.html#alerts
 * @author lguerin
 */
public enum BootstrapAlertTypeEnum
{
    INFO, SUCCESS, ERROR;

    public String toString()
    {
        return super.toString().toLowerCase();
    };
}
