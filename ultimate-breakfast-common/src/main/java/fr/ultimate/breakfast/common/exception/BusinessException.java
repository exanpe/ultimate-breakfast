/**
 * 
 */
package fr.ultimate.breakfast.common.exception;

/**
 * La classe <code>BusinessException</code> represente une exception fonctionnelle qu'il est
 * possible de récupérer depuis l'interface IHM.
 * 
 * @author lguerin
 */
public class BusinessException extends Exception
{
    /**
     * serialUid
     */
    private static final long serialVersionUID = -8229837850658777788L;

    /**
     * Construit un objet <code>BusinessException</code>.
     */
    public BusinessException()
    {
        super();
    }

    /**
     * Construit un objet <code>BusinessException</code>.
     * 
     * @param message le message d'erreur
     * @param cause cause de l'exception
     */
    public BusinessException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Construit un objet <code>BusinessException</code>.
     * 
     * @param message le message d'erreur
     */
    public BusinessException(String message)
    {
        super(message);
    }

    /**
     * Construit un objet <code>BusinessException</code>.
     * 
     * @param cause cause de l'exception
     */
    public BusinessException(Throwable cause)
    {
        super(cause);
    }
}
