/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import org.apache.tapestry5.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.web.constants.JsConsoleCommandEnum;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;

/**
 * @author lguerin
 */
@Service
@Scope("prototype")
public class DelUserCommand extends AbstractJsConsoleCommand
{

    /*
     * (non-Javadoc)
     * @see
     * fr.ultimate.breakfast.web.services.jsConsole.Command#execute(org.apache.tapestry5.json.JSONArray
     * )
     */
    @Override
    public JsConsoleResponseHolder execute(JSONArray args)
    {
        // Validate deluser args
        if (args.length() != 1) { return commandArgsMatchError(JsConsoleCommandEnum.DELUSER); }

        // Validate username
        String username = args.getString(0);
        Team current = securityContext.getTeam();
        Eater eater = manager.findEaterByName(current, username);
        if (eater == null) { return holder.add("deluser-ko-msg").error(); }
        manager.deleteEater(eater.getId());
        return holder.add("deluser-ok-msg");
    }

}
