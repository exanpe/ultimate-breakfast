/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import java.util.HashSet;
import java.util.Set;

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
public class SendMailCommand extends AbstractJsConsoleCommand
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
        // Validate sendmail args
        if (args.length() < 1) { return commandArgsMatchError(JsConsoleCommandEnum.ADDUSER); }

        // Validate list of username
        Team current = securityContext.getTeam();
        boolean ok = true;
        Set<String> mails = new HashSet<String>();
        for (int i = 0; i < args.length(); i++)
        {
            String username = args.getString(i);
            Eater eater = manager.findEaterByName(current, username);
            if (eater == null)
            {
                ok = false;
                break;
            }
            mails.add(eater.getEmail());
        }

        if (!ok) { return holder.add("sendmail-ko-msg").error(); }

        manager.notifyEaters(current, mails, null);
        return holder.add("sendmail-ok-msg");
    }

}
