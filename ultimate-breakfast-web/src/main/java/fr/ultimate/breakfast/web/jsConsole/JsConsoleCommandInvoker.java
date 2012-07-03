/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;
import fr.ultimate.breakfast.web.constants.JsConsoleCommandEnum;
import fr.ultimate.breakfast.web.jsConsole.impl.AddUserCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.DelUserCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.HelpCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.LoginCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.LogoutCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.LsCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.PositionCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.RenameUserCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.SendMailCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.TopUserCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.ValidateCommand;
import fr.ultimate.breakfast.web.jsConsole.impl.WhoAmICommand;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;

/**
 * @author lguerin
 */
@Service
public class JsConsoleCommandInvoker
{
    private final Map<JsConsoleCommandEnum, Class<? extends Command>> registry;

    @Autowired
    protected UltimateBreakfastSecurityContext securityContext;

    @Autowired
    private ApplicationContext applicationContext;

    public JsConsoleCommandInvoker() throws InstantiationException, IllegalAccessException
    {
        registry = new HashMap<JsConsoleCommandEnum, Class<? extends Command>>();
        registry.put(JsConsoleCommandEnum.LS, LsCommand.class);
        registry.put(JsConsoleCommandEnum.LOGIN, LoginCommand.class);
        registry.put(JsConsoleCommandEnum.LOGOUT, LogoutCommand.class);
        registry.put(JsConsoleCommandEnum.HELP, HelpCommand.class);
        registry.put(JsConsoleCommandEnum.WHOAMI, WhoAmICommand.class);
        registry.put(JsConsoleCommandEnum.ADDUSER, AddUserCommand.class);
        registry.put(JsConsoleCommandEnum.TOPUSER, TopUserCommand.class);
        registry.put(JsConsoleCommandEnum.DELUSER, DelUserCommand.class);
        registry.put(JsConsoleCommandEnum.RENAMEUSER, RenameUserCommand.class);
        registry.put(JsConsoleCommandEnum.POSITION, PositionCommand.class);
        registry.put(JsConsoleCommandEnum.SENDMAIL, SendMailCommand.class);
        registry.put(JsConsoleCommandEnum.VALIDATE, ValidateCommand.class);
    }

    public JsConsoleResponseHolder invoke(String command, JSONArray args)
    {
        JsConsoleResponseHolder holder = JsConsoleResponseHolder.create();
        JsConsoleCommandEnum commandType = JsConsoleCommandEnum.fromCommand(command);
        if (securityContext.isLoggedIn() || isSpecialCommand(commandType))
        {
            holder = invokeCommand(commandType, args);
        }
        else
        {
            holder.add("auth-ko-msg").error();
        }
        return holder;
    }

    private boolean isSpecialCommand(JsConsoleCommandEnum commandType)
    {
        return (JsConsoleCommandEnum.LOGIN.equals(commandType) || JsConsoleCommandEnum.HELP.equals(commandType));
    }

    private JsConsoleResponseHolder invokeCommand(JsConsoleCommandEnum commandType, JSONArray args)
    {
        JsConsoleResponseHolder holder = JsConsoleResponseHolder.create();
        if (registry.containsKey(commandType))
        {
            Command c = (Command) applicationContext.getBean(registry.get(commandType));
            holder = c.execute(args);
        }
        else
        {
            holder.add("unknow-command-msg");
        }
        return holder;
    }
}
