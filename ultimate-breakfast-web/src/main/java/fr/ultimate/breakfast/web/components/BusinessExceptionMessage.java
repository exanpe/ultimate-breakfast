package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

import fr.ultimate.breakfast.common.exception.BusinessException;
import fr.ultimate.breakfast.web.services.exceptionHandler.ExceptionHandlerService;

/**
 * Displays a message caused by a {@link BusinessException}
 * 
 * @author jmaupoux
 */
public class BusinessExceptionMessage
{
    @Inject
    private Logger logger;

    @Inject
    private Request request;

    @Inject
    private ComponentResources resources;

    @Property
    private String message;

    @SetupRender
    boolean init()
    {
        String businessMessage = (String) request.getAttribute(ExceptionHandlerService.REQ_BUSINESS_MESSAGE);

        if (businessMessage == null)
            return false;

        if (resources.getMessages().contains(businessMessage))
        {
            message = resources.getMessages().get(businessMessage);
        }
        else
        {
            message = businessMessage;
        }

        logger.debug("Displaying BusinessException message {}", message);

        return true;
    }
}
