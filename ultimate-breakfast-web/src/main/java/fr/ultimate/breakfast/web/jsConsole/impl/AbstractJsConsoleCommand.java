/**
 * 
 */
package fr.ultimate.breakfast.web.jsConsole.impl;

import org.apache.tapestry5.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;
import fr.ultimate.breakfast.web.constants.JsConsoleCommandEnum;
import fr.ultimate.breakfast.web.jsConsole.Command;
import fr.ultimate.breakfast.web.util.JsConsoleResponseHolder;

/**
 * @author lguerin
 */
@Service
@Scope("prototype")
public abstract class AbstractJsConsoleCommand implements Command
{
    protected JsConsoleResponseHolder holder;
    protected final static String ARGS_SEPARATOR = "|";

    @Autowired
    protected BreakfastManager manager;

    @Autowired
    protected UltimateBreakfastSecurityContext securityContext;

    public AbstractJsConsoleCommand()
    {
        holder = JsConsoleResponseHolder.create();
    }

    protected JsConsoleResponseHolder commandArgsMatchError(JsConsoleCommandEnum commandType)
    {
        return holder.add(
                "Les arguments fournis a la commande: '" + commandType + "' ne sont pas corrects.\nUtilisez la commande 'help' pour obtenir de l'aide.")
                .error();
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.ultimate.breakfast.web.services.jsConsole.Command#execute(org.apache.tapestry5.json.JSONArray
     * )
     */
    @Override
    public abstract JsConsoleResponseHolder execute(JSONArray args);
}
