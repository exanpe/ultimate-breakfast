/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole;

import org.apache.tapestry5.json.JSONArray;

import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;

/**
 * @author lguerin
 */
public interface Command
{
    JsConsoleResponseHolder execute(JSONArray args);
}
