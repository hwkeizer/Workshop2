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
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.CustomerService;
import workshop2.persistencelayer.GenericDaoImpl;

/**
 *
 * @author hwkei
 */
public class CustomerServiceHibernate extends GenericServiceHibernate implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceHibernate.class);

    @Override
    public void createCustomer(Customer customer) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl customerDao = new GenericDaoImpl(Customer.class, em);
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch(Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );           
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
                
    }
    
    @Override
    public void updateCustomer(Customer customer) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl customerDao = new GenericDaoImpl(Customer.class, em);
        try {
            em.getTransaction().begin();
            customerDao.update(customer);
            em.getTransaction().commit();
        } catch(Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );       
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteCustomer(Customer customer) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl customerDao = new GenericDaoImpl(Customer.class, em);
        try {   
            em.getTransaction().begin();   
            customerDao.delete(em.find(Customer.class, customer.getId()));            
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
    public Optional<Customer> findCustomerByLastName(String lastName) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        Customer resultCustomer;
        try {
            Query queryCustomerByLastName = em.createNamedQuery("findCustomerByLastName");
            queryCustomerByLastName.setParameter("lastName", lastName);
            resultCustomer = (Customer)queryCustomerByLastName.getSingleResult();
        } catch(NoResultException ex) {
            log.debug("Account with username {} is not found in the database", lastName);
            return Optional.empty();
        } finally {
            em.close();
        }
        return Optional.ofNullable(resultCustomer);
    }
    
    
}
