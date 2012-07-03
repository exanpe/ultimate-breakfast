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
public class RenameUserCommand extends AbstractJsConsoleCommand
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
        if (args.length() != 2) { return commandArgsMatchError(JsConsoleCommandEnum.RENAMEUSER); }

        Team current = securityContext.getTeam();
        // Validate username
        String username = args.getString(0);
        Eater eater = manager.findEaterByName(current, username);
        if (eater == null) { return holder.add("renameuser-username-ko-msg").error(); }

        // Validate new username
        String newUsername = args.getString(1);
        Eater newEater = manager.findEaterByName(current, newUsername);
        if (newEater != null) { return holder.add("renameuser-newusername-ko-msg").error(); }

        // Update user
        eater.setName(newUsername);
        manager.updateEater(eater);
        return holder.add("renameuser-ok-msg");
    }
}
