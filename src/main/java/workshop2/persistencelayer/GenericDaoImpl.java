/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author hwkei
 * @param <T>
 */
public class GenericDaoImpl<T> {
    
    protected EntityManager entityManager;
    protected final Class<T> entityClass;

    /**
     * Constructor with EntityManager injection
     * @param entityClass
     * @param entityManager
     */
    public GenericDaoImpl(Class<T> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    /**
     *
     * @param persistedObject
     */
    public void persist(T persistedObject) {        
        entityManager.persist(persistedObject);        
    }

    /**
     *
     * @param updatedObject
     * @return
     */
    public T update(T updatedObject) {
        return entityManager.merge(updatedObject);
    }

    /**
     *
     * @param id
     * @return
     */
    public T findById(Long id) {
        return entityManager.find(entityClass, id);
    }

    /**
     *
     * @param type
     * @return
     */
    public List<T> findAll(Class<T> type) {
        CriteriaQuery<T> c = entityManager.getCriteriaBuilder().createQuery(type);
        c.select(c.from(type));
        return entityManager.createQuery(c).getResultList();
    }

    /**
     *
     * @param deletedObject
     */
    public void delete(T deletedObject) {
        entityManager.remove(deletedObject);
    }  
}
