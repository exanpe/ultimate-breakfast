package fr.ultimate.breakfast.web.util;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import fr.ultimate.breakfast.domain.core.business.DefaultManager;

/**
 * La classe <code>EntityGridDataSource</code> fournit les données pour les grilles d'objets
 * persistents.
 * 
 * @param <T> le type d'objets fournis
 */
@SuppressWarnings("rawtypes")
public class EntityGridDataSource<T> implements GridDataSource
{
    /**
     * Type d'objets fournis
     */
    private Class<T> entityClass;

    /**
     * Données affichées
     */
    private List<T> currentData;

    /**
     * Indice de début de la liste extraite
     */
    private int start;

    /**
     * Indice de fin de la liste extraite
     */
    private int end;

    /**
     * Gestionnaire d'objets métiers <code>T</code>
     */
    private DefaultManager provider;

    /**
     * Requête à exécuter
     */
    private String namedQuery;

    /**
     * Paramètres de la requête
     */
    private Map<String, Object> params;

    /**
     * Construit un objet <code>EntityGridDataSource</code>.
     * 
     * @param newEntityClass le type d'objets à fournir
     * @param newProvider le gestionnaire d'objets métiers
     */
    public EntityGridDataSource(Class<T> newEntityClass, DefaultManager newProvider, String namedQuery)
    {
        this.entityClass = newEntityClass;
        this.provider = newProvider;
        this.namedQuery = namedQuery;
    }

    @Override
    public int getAvailableRows()
    {
        return (int) provider.count();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints)
    {
        // extraction des données
        currentData = provider.findRangeResultsWithNamedQuery(this.namedQuery, 0, 5, params);
        start = startIndex;
        end = endIndex;
    }

    @Override
    public Object getRowValue(int index)
    {
        T value = null;
        if (index >= start && index <= end && (index - start < currentData.size()))
        {
            value = currentData.get(index - start);
        }
        return value;
    }

    @Override
    public Class<T> getRowType()
    {
        return entityClass;
    }
}
