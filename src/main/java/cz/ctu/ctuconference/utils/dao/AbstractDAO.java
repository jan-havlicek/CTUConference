/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.utils.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

/**
 *
 * @author Nick nemame
 */
abstract public class AbstractDAO<ENTITY extends AbstractEntity> implements DAOInterface {

    private Class<ENTITY> type;

    public AbstractDAO() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    protected String getClassName() {
        return type.getSimpleName();
    }

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Override
    public ENTITY getById(long id) {
        return (ENTITY) getEntityManager().find(type, id);
    }

    @Override
    public void removeById(long id) {
        getEntityManager().remove(getEntityManager().getReference(type, id));
    }

    @Override
    public List<ENTITY> getAll() {
        String query = "SELECT e FROM "+getClassName()+" e";
        return getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<ENTITY> getPage(int page, int resultsCount) {
        String query = "SELECT e FROM "+getClassName()+" e";
        return getEntityManager().createQuery(query).setFirstResult(page * resultsCount).setMaxResults(resultsCount).getResultList();
    }

    @Override
    public int getCount() {
        String query = "SELECT count(e) FROM "+getClassName()+" e";
        return ((Integer) getEntityManager().createQuery(query).getSingleResult()).intValue();
    }

    @Override
    public boolean exists(long id) {
        String queryString = "SELECT COUNT(e) FROM "+getClassName()+" e WHERE e.id = :value";
        return (((Long) getEntityManager().createQuery(queryString).setParameter("value", id).getSingleResult()).longValue() == 0) ? false : true;
    }

     /**
     * Get entity manager for the current transaction
     * @return
     */
    protected EntityManager getEntityManager() {
        return EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
    }

  /*
    protected EntityManager getEntityManager() {
        return Persistence.createEntityManagerFactory("mysqlPersistenceUnit").createEntityManager();
    }*/

    public void persist(ENTITY entity) {
        getEntityManager().persist(entity);
    }

    public List<ENTITY> getAllSortedBy(String property) {
        String queryString = "SELECT e FROM "+getClassName()+" e ORDER BY e."+property;
        return getEntityManager().createQuery(queryString).getResultList();
    }

    public List<ENTITY> getByProperty(String property, Object value) {
        String queryString = "SELECT e FROM "+getClassName()+" e WHERE e." + property + " = :value";
        return getEntityManager().createQuery(queryString).setParameter("value", value).getResultList();
    }

    public boolean existsWithProperty(String property, Object value) {
        String queryString = "SELECT COUNT(e) FROM "+getClassName()+" e WHERE e."+property+" = :value";
        System.out.println(queryString);
        return (((Long) getEntityManager().createQuery(queryString).setParameter("value", (String) value).getSingleResult()).longValue() == 0) ? false : true;
    }

    public boolean existsWithPropertyExcludeIdentity(String property, Object value, Long id) {
        String queryString = "SELECT COUNT(e) FROM "+getClassName()+" e WHERE e."+property+" = :value AND e.id <> :id";
        System.out.println(queryString);
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("value", value);
        query.setParameter("id", id);
        return (((Long) query.getSingleResult()).longValue() == 0) ? false : true;
    }

    public void merge(ENTITY entity) {
        getEntityManager().merge(entity);

    }

    public void flush() {
        getEntityManager().flush();
    }

    public void refresh(ENTITY entity) {
        getEntityManager().refresh(entity);
    }

    public void rollback() {
        getEntityManager().getTransaction().rollback();
    }
}
