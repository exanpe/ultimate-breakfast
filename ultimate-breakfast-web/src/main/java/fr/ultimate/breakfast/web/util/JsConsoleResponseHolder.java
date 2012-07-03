/**
 * 
 */
package fr.ultimate.breakfast.web.util;

import org.apache.tapestry5.json.JSONArray;

import fr.ultimate.breakfast.web.components.JsConsole;
import fr.ultimate.breakfast.web.constants.JsConsoleStatusEnum;

/**
 * @author lguerin
 */
public class JsConsoleResponseHolder
{
    /**
     * Results displayed into {@link JsConsole} output
     */
    private JSONArray responses = null;

    /**
     * Status of the response
     */
    private JsConsoleStatusEnum status;

    private JsConsoleResponseHolder()
    {
        responses = new JSONArray();
        status = JsConsoleStatusEnum.SUCCESS;
    }

    public static JsConsoleResponseHolder create()
    {
        return new JsConsoleResponseHolder();
    }

    public JsConsoleResponseHolder add(String response)
    {
        responses.put(response);
        return this;
    }

    public JSONArray getResponses()
    {
        return this.responses;
    }

    public boolean hasResponses()
    {
        return this.responses.length() > 0;
    }

    public JsConsoleStatusEnum getStatus()
    {
        return this.status;
    }

    public JsConsoleResponseHolder error()
    {
        this.status = JsConsoleStatusEnum.ERROR;
        return this;
    }
}
