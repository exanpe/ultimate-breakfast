package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.ultimate.breakfast.web.constants.BootstrapAlertTypeEnum;

/**
 * Wrapper for Bootstrap Alerts
 * 
 * @see http://twitter.github.com/bootstrap/components.html#alerts
 * @author lguerin
 */
@SupportsInformalParameters
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/animation/animation-min.js" })
public class BootstrapAlert implements ClientElement
{
    /**
     * Specify the type of alert
     * 
     * @see BootstrapAlertTypeEnum
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(value = "info", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private BootstrapAlertTypeEnum type;

    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private boolean temporary;

    @Inject
    private ComponentResources componentResources;

    @Override
    public String getClientId()
    {
        return componentResources.getId();
    }
}
