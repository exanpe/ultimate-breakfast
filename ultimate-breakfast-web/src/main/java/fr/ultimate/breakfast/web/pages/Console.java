package fr.ultimate.breakfast.web.pages;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.ultimate.breakfast.web.jsConsole.JsConsoleCommandInvoker;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;
import fr.ultimate.breakfast.web.util.UltimateBreakfastEvents;

/**
 * Console popup
 */
@Import(library =
{ "context:/layout/js/modernizr/2.5.3/modernizr-2.5.3-min.js" })
public class Console
{
    @Inject
    private JsConsoleCommandInvoker invoker;

    @OnEvent(value = UltimateBreakfastEvents.JSCONSOLE_COMMAND_EVENT)
    JsConsoleResponseHolder onJsConsoleEvent(String command, JSONArray args)
    {
        return invoker.invoke(command, args);
    }

    @Inject
    private RequestGlobals request;

    @Environmental
    private JavaScriptSupport jsSupport;

    @PageLoaded
    public void consoleLoaded()
    {
        request.getHTTPServletRequest().getSession(true);
    }

    public String getMotD()
    {
        String motd = " ";
        motd += " __  __ __ __   _              __         ___                   __    ____           __  \n";
        motd += " / / / // // /_ (_)__ _  ___ _ / /_ ___   / _ ) ____ ___  ___ _ / /__ / __/___ _ ___ / /_ \n";
        motd += "/ /_/ // // __// //  ' \\/ _ `// __// -_) / _  |/ __// -_)/ _ `//  '_// __// _ `/(_- / __/\n";
        motd += "\\____//_/ \\__//_//_/_/_/\\_,_/ \\__/ \\__/ /____//_/   \\__/ \\_,_//_/\\_\\/_/   \\_,_/___/ \\__/ \n";
        motd += "\n";
        return motd;
    }
}
