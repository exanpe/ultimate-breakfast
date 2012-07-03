package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Wrapper for Bootstrap Alerts
 * 
 * @see http://twitter.github.com/bootstrap/components.html#alerts
 * @author lguerin
 */
@SupportsInformalParameters
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/animation/animation-min.js" })
public class OldBrowserMessage
{

    @Inject
    private JavaScriptSupport jsSupport;

    @AfterRender
    void end()
    {
        jsSupport.addInitializerCall("oldBrowserMessageBuilder", "");
    }
}
