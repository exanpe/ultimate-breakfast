/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
public class ValidateCommand extends AbstractJsConsoleCommand
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
        // Validate "validate" command args
        if (args.length() < 3) { return commandArgsMatchError(JsConsoleCommandEnum.VALIDATE); }

        Team current = securityContext.getTeam();
        boolean ok = true;
        boolean separator = false;
        List<Eater> suppliers = new LinkedList<Eater>();
        List<Eater> absents = new LinkedList<Eater>();
        for (int i = 0; i < args.length(); i++)
        {
            String el = args.getString(i);

            // Handle separator element
            if (el.equals(ARGS_SEPARATOR))
            {
                if (separator) { return commandArgsMatchError(JsConsoleCommandEnum.VALIDATE); }
                separator = true;
            }
            else
            {
                // Validate eater
                Eater eater = manager.findEaterByName(current, el);
                if (eater == null)
                {
                    ok = false;
                    break;
                }

                // Filter suppliers and absents
                if (separator)
                {
                    absents.add(eater);
                }
                else
                {
                    suppliers.add(eater);
                }
            }
        }

        // Check suppliers and absents coherence
        if (CollectionUtils.intersection(suppliers, absents).size() > 0) { return holder.add("validate-intersect-ko-msg").error(); }
        if (!ok) { return holder.add("validate-ko-msg").error(); }

        // Finally, validate breakfast
        manager.commit(current, suppliers, absents);
        return holder.add("validate-ok-msg");
    }
}
