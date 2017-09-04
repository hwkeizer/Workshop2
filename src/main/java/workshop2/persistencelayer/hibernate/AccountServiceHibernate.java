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
public class AccountServiceHibernate implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceHibernate.class);
    private final EntityManager entityManager;
    private final GenericDaoImpl accountDao;
    
    public AccountServiceHibernate() {
        entityManager = DatabaseConnection.getInstance().getEntityManager();
        accountDao = new GenericDaoImpl(Account.class, entityManager);
    }
    
    @Override
    public void createAccount(Account account) {
        try {
            entityManager.getTransaction().begin();       
            accountDao.persist(account);            
            entityManager.getTransaction().commit();            
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            System.out.println("Transactie is niet uitgevoerd!");            
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            // Always clear the persistence context to prevent increasing memory ????
            entityManager.clear();
        }
    }
    
    @Override
    public void deleteAccount(Account account) {
        try { 
            entityManager.getTransaction().begin();       
            accountDao.delete(account);            
            entityManager.getTransaction().commit();            
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );           
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            // Always clear the persistence context to prevent increasing memory ????
            entityManager.clear();
        }
    }
    
    @Override
    public void updateAccount(Account account) {
        try {
            entityManager.getTransaction().begin();
            accountDao.update(account);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );       
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            // Always clear the persistence context to prevent increasing memory ????
            entityManager.clear();
            log.debug("ENTITYMANAGER CLEARED");
        }
    }            
    
    @Override
    public Optional<Account> findAccountByUserName(String userName) {
        Account resultAccount;
        try {
            Query queryAccountByUserName = entityManager.createNamedQuery("findAccountByUserName");
            queryAccountByUserName.setParameter("username", userName);
            resultAccount = (Account)queryAccountByUserName.getSingleResult();
        } catch(NoResultException ex) {
            log.debug("Username {} is not found in the database", userName);
            return Optional.empty();
        }
        return Optional.ofNullable(resultAccount);
    }
    
    @Override
    public Optional<Account> findAccountById(Long id) {
        return Optional.ofNullable((Account)accountDao.findById(id));
    }
    
    @Override
    public List<Account> findAllAccounts() {
        return accountDao.findAll(Account.class);
    }
}
