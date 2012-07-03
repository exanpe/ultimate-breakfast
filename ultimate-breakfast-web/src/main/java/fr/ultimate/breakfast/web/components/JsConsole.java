/**
 * 
 */
package fr.ultimate.breakfast.web.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.services.ExanpeComponentService;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;
import fr.ultimate.breakfast.web.util.UltimateBreakfastEvents;

/**
 * Une console Javascript permettant d'executer des commandes à la manière d'une console shell
 * Design largement inspiré de http://josscrowcroft.github.com/javascript-sandbox-console/
 * 
 * @see http://josscrowcroft.github.com/javascript-sandbox-console/
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/connection/connection-min.js", "${exanpe.yui2-base}/json/json-min.js",
        "context:/layout/js/ultimate-breakfast.js" }, stylesheet =
{ "css/jsconsole.css" })
public class JsConsole
{
    private static final String ROOT_CSS_CLASS = "jsconsole";

    @Property
    private String uniqueId;

    @Property
    private Link link;

    /**
     * The message displayed into placeholder
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
    private String placeholder;

    /**
     * A comma-separated list of commands to be confirmed
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String confirmCommands;

    /**
     * The size of the command history
     */
    @Property
    @Parameter(value = "10", defaultPrefix = BindingConstants.LITERAL)
    private int historySize;

    /**
     * Message of the day
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String motd;

    /**
     * Event triggered in Ajax.
     */
    private static final String EVENT_NAME = "executeCommandEvent";

    /**
     * Param used to get command parameter from the query.
     */
    public static final String PARAM_COMMAND_NAME = "command";

    /**
     * Param used to get args parameter from the query.
     */
    public static final String PARAM_ARGS_NAME = "args";

    @Inject
    private JavaScriptSupport jsSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private ExanpeComponentService ecservice;

    @Inject
    private Logger log;

    @SetupRender
    void init()
    {
        uniqueId = jsSupport.allocateClientId(resources);
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");
        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);
        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();
        link = resources.createEventLink(EVENT_NAME);
        JSONObject data = buildJSONData();
        jsSupport.addInitializerCall("jsConsoleBuilder", data);
    }

    @OnEvent(value = EVENT_NAME)
    Object onReceiveCommand(@RequestParameter(PARAM_COMMAND_NAME)
    String command, @RequestParameter(PARAM_ARGS_NAME)
    String newArgs)
    {
        log.debug("Command received: {}", command);
        JSONArray args = new JSONArray(newArgs);
        log.debug("Args received: {}", args);
        final Holder<JsConsoleResponseHolder> holder = Holder.create();
        final ComponentEventCallback<JsConsoleResponseHolder> callback = new ComponentEventCallback<JsConsoleResponseHolder>()
        {
            public boolean handleResult(final JsConsoleResponseHolder result)
            {
                holder.put(result);
                return true;
            }
        };

        log.debug("Triggering event to container...");
        resources.triggerEvent(UltimateBreakfastEvents.JSCONSOLE_COMMAND_EVENT, new Object[]
        { command, args }, callback);

        log.debug("Sending result from container...");
        final JsConsoleResponseHolder responseHolder = (JsConsoleResponseHolder) holder.get();
        return generateI18nResponses(responseHolder);
    }

    private JSONObject generateI18nResponses(JsConsoleResponseHolder holder)
    {
        JSONObject result = new JSONObject();
        JSONArray responses = new JSONArray();
        for (int i = 0; i < holder.getResponses().length(); i++)
        {
            String msg = (String) holder.getResponses().get(i);
            String value = resources.getContainerResources().getMessages().contains(msg) ? resources.getContainerResources().getMessages().get(msg) : msg;
            responses.put(value);
        }
        result.put("status", holder.getStatus().toString());
        result.put("response", responses);
        return result;
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("url", link.toURI());
        data.accumulate("historySize", historySize);

        // Commands to be confirmed
        JSONArray cmds = new JSONArray();
        if (confirmCommands != null)
        {
            String[] list = confirmCommands.split(",");
            for (int i = 0; i < list.length; i++)
            {
                cmds.put(StringUtils.strip(list[i]).toLowerCase());
            }
        }
        data.accumulate("confirmCommands", cmds.toString());
        data.accumulate("motd", motd);

        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
