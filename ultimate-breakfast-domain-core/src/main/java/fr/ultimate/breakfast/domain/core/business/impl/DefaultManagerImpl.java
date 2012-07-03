package fr.ultimate.breakfast.domain.core.business.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.ultimate.breakfast.domain.core.business.DefaultManager;
import fr.ultimate.breakfast.domain.core.dao.CrudDAO;

/**
 * Implémentation générique de l'interface {@link DefaultManager}.
 * 
 * @author lguerin
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public abstract class DefaultManagerImpl<T, PK extends Serializable> implements DefaultManager<T, PK>
{

    @Autowired
    private CrudDAO crudDAO;

    /**
     * Le type de l'objet courant.
     */
    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public DefaultManagerImpl()
    {
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T create(T t)
    {
        Assert.notNull(t, "L'objet transmis ne peut etre null");
        return crudDAO.create(t);
    }

    @Override
    public T update(T t)
    {
        return crudDAO.update(t);
    }

    @Override
    public void delete(PK id)
    {
        Assert.notNull(id, "L'identifiant de l'objet ne peut etre null");
        crudDAO.delete(entityType, id);
    }

    @Override
    @Transactional(readOnly = true)
    public T find(PK id)
    {
        Assert.notNull(id, "L'identifiant de l'objet ne peut etre null");
        return crudDAO.find(entityType, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findRangeResultsWithNamedQuery(String queryName, int startIndex, int endIndex, Map<String, Object> params)
    {
        return crudDAO.findRangeResultsWithNamedQuery(queryName, startIndex, endIndex, params);
    }

    @Override
    public long count()
    {
        return crudDAO.count(entityType);
    }

    @Override
    public long count(String queryName, Map<String, Object> params)
    {
        return crudDAO.count(queryName, params);
    }

}
