/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
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
public class AddUserCommand extends AbstractJsConsoleCommand
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
        // Validate adduser args
        if (args.length() < 2 || args.length() > 3) { return commandArgsMatchError(JsConsoleCommandEnum.ADDUSER); }
        holder = validateArgs(args);
        if (holder.hasResponses()) { return holder; }

        // Validate username
        String username = args.getString(0);
        Team current = securityContext.getTeam();
        Eater exist = manager.findEaterByName(current, username);
        if (exist != null) { return holder.add("adduser-ko-msg").error(); }

        // Create new eater
        Eater eater = generateEater(current, args);
        manager.addEater(current, eater);
        return holder.add("adduser-ok-msg");
    }

    private Eater generateEater(Team team, JSONArray args)
    {
        String name = args.getString(0);
        String email = args.getString(1);

        // Handle eater position
        Integer position = null;
        if (args.length() == 3)
        {
            position = Integer.valueOf(args.getString(2));
        }
        if (position == null)
        {
            position = manager.getNextMaxPosition(team);
        }

        // Create new eater
        Eater eater = new Eater();
        eater.setName(name);
        eater.setEmail(email);
        eater.setPosition(position);

        return eater;
    }

    private JsConsoleResponseHolder validateArgs(JSONArray args)
    {
        JsConsoleResponseHolder holder = JsConsoleResponseHolder.create();
        if (!EmailValidator.getInstance().isValid(args.getString(1)))
        {
            holder.add("adduser-ko-email-msg").error();
        }
        if (args.length() == 3)
        {
            if (!GenericValidator.isInt(args.getString(2)))
            {
                holder.add("adduser-ko-position-msg").error();
            }
        }
        return holder;
    }
}
