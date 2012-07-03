package fr.ultimate.breakfast.domain.core.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe Helper pour l'utilisation des paramètres associés aux requêtes JPQL nommées :
 * <code>@NamedQuery</code>.
 * 
 * @author lguerin
 */
public class QueryParameters
{
    private Map<String, Object> parameters = null;

    private QueryParameters(String name, Object value)
    {
        this.parameters = new HashMap<String, Object>();
        this.parameters.put(name, value);
    }

    public static QueryParameters with(String name, Object value)
    {
        return new QueryParameters(name, value);
    }

    public QueryParameters and(String name, Object value)
    {
        this.parameters.put(name, value);
        return this;
    }

    public Map<String, Object> parameters()
    {
        return this.parameters;
    }
}
