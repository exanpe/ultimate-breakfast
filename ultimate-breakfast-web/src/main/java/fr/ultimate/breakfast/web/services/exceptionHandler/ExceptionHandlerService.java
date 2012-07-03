package fr.ultimate.breakfast.web.services.exceptionHandler;

/**
 * Service handling everything about Exceptions.
 * Constants + utilities services
 * 
 * @author jmaupoux
 */
public class ExceptionHandlerService
{
    /**
     * Le message à afficher sur une BusinessException
     */
    public static final String REQ_BUSINESS_MESSAGE = "businessExceptionMessage";

    /**
     * Page d'erreur affichée après interception de l'exception
     */
    public static final String ERROR_PAGE = "ApplicationError";

    /**
     * Methode récursive permettant de pointer l'exception réelle.
     * 
     * @param <T> T
     * @param e e
     * @param type type
     * @return l'exception réelle
     */
    public <T extends Throwable> T getNestedExceptionOfType(Throwable e, Class<T> type)
    {
        T result = null;
        if (e != null)
        {
            if (type.isAssignableFrom(e.getClass()))
            {
                result = (T) e;
            }
            else
            {
                result = getNestedExceptionOfType(e.getCause(), type);
            }
        }
        return result;
    }
}
