package fr.ultimate.breakfast.common.exception;

/**
 * La classe <code>TechnicalException</code> represente une exception technique.
 * Cette exception signale qu'une erreur technique non prevue par le systeme s'est produite.
 * 
 * @author lguerin
 */
public class TechnicalException extends RuntimeException
{
    /**
     * serialUid
     */
    private static final long serialVersionUID = -8316324009963694711L;

    /**
     * Construit un objet <code>TechnicalException</code>.
     */
    public TechnicalException()
    {
        super();
    }

    /**
     * Construit un objet <code>TechnicalException</code>.
     * 
     * @param message le message d'erreur
     * @param source l'exception source
     */
    public TechnicalException(String message, Throwable source)
    {
        super(message, source);
    }

    /**
     * Construit un objet <code>TechnicalException</code>.
     * 
     * @param message le message d'erreur
     */
    public TechnicalException(String message)
    {
        super(message);
    }

    /**
     * Construit un objet <code>TechnicalException</code>.
     * 
     * @param source l'exception source
     */
    public TechnicalException(Throwable source)
    {
        super(source);
    }
}
