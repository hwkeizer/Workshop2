/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer.hibernate;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.GenericDaoImpl;
import workshop2.persistencelayer.GenericService;

/**
 *
 * @author hwkei
 */
public class GenericServiceHibernate {
    private static final Logger log = LoggerFactory.getLogger(GenericServiceHibernate.class);
    
    public <T> List<T> fetchAllAsList(Class<T> entityClass) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl genericDao = new GenericDaoImpl(entityClass, em);
        List<T> allList;
        try {
            allList =genericDao.findAll();
        } catch(NoResultException ex) {
            log.debug("No entities found in the database");
            return null;
        } finally {
            em.close();
        }
        return allList;
    }
    
    public <T> Optional<T> fetchById(Class<T> entityClass, Long id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl genericDao = new GenericDaoImpl(entityClass, em);
        Optional<T> fetchedObject;
        try {
            fetchedObject = genericDao.findById(id);
        } catch(NoResultException ex) {
            log.debug("No entities found in the database");
            return null;
        } finally {
            em.close();
        }

        return fetchedObject;
    }
}
