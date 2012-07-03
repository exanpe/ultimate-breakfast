/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import org.apache.tapestry5.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.ultimate.breakfast.web.constants.JsConsoleCommandEnum;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;

/**
 * @author lguerin
 */
@Service
@Scope("prototype")
public class HelpCommand extends AbstractJsConsoleCommand
{
    private static final String HELP_PREFIX = "help-";
    private static final String HELP_SUFFIX = "-msg";

    /*
     * (non-Javadoc)
     * @see
     * fr.ultimate.breakfast.web.services.jsConsole.Command#execute(org.apache.tapestry5.json.JSONArray
     * )
     */
    @Override
    public JsConsoleResponseHolder execute(JSONArray args)
    {
        if (args.length() > 1) { return commandArgsMatchError(JsConsoleCommandEnum.HELP); }
        if (args.length() == 1)
        {
            JsConsoleCommandEnum command = JsConsoleCommandEnum.fromCommand(args.getString(0));
            if (command != null)
            {
                holder.add(HELP_PREFIX + command.toString() + HELP_SUFFIX);
            }
            else
            {
                holder.add(HELP_PREFIX + "error" + HELP_SUFFIX);
            }
        }
        else
        {
            for (JsConsoleCommandEnum command : JsConsoleCommandEnum.values())
            {
                String help = buildHelpMsg(command);
                holder.add(help);
            }
            holder.add(HELP_PREFIX + "clear" + HELP_SUFFIX);
        }
        return holder;
    }

    private String buildHelpMsg(JsConsoleCommandEnum command)
    {
        String res = command == JsConsoleCommandEnum.HELP ? "default" : command.toString();
        return HELP_PREFIX + res + HELP_SUFFIX;
    }
}
