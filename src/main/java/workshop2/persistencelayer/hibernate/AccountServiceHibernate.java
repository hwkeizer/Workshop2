/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer.hibernate;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.AccountService;
import workshop2.persistencelayer.GenericDaoImpl;

/**
 *
 * @author hwkei
 */
public class AccountServiceHibernate extends GenericServiceHibernate implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceHibernate.class);
        
    @Override
    public void createAccount(Account account) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl accountDao = new GenericDaoImpl(Account.class, em);
        try {
            em.getTransaction().begin();       
            accountDao.persist(account);            
            em.getTransaction().commit();            
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );           
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
    }
    
    @Override
    public void deleteAccount(Account account) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl accountDao = new GenericDaoImpl(Account.class, em);
        try {   
            em.getTransaction().begin();   
            accountDao.delete(em.find(Account.class, account.getId()));            
            em.getTransaction().commit();            
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );           
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
    }
    
    @Override
    public void updateAccount(Account account) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl accountDao = new GenericDaoImpl(Account.class, em);
        try {
            em.getTransaction().begin();
            accountDao.update(account);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );       
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
    }            
    
    @Override
    public Optional<Account> findAccountByUserName(String userName) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        Account resultAccount;
        try {
            Query queryAccountByUserName = em.createNamedQuery("findAccountByUserName");
            queryAccountByUserName.setParameter("username", userName);
            resultAccount = (Account)queryAccountByUserName.getSingleResult();
        } catch(NoResultException ex) {
            log.debug("Account with username {} is not found in the database", userName);
            return Optional.empty();
        } finally {
            em.close();
        }
        return Optional.ofNullable(resultAccount);
    }
    
}
