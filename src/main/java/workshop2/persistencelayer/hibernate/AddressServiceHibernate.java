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
import workshop2.domain.Address;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.AddressService;
import workshop2.persistencelayer.GenericDaoImpl;


/**
 *
 * @author Al-Alaaq(Egelantier)
 */
public class AddressServiceHibernate extends GenericServiceHibernate implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceHibernate.class);

    private EntityManager entityManager;

    private GenericDaoImpl addressDao;

    public AddressServiceHibernate() {

        entityManager = DatabaseConnection.getInstance().getEntityManager();

        addressDao = new GenericDaoImpl(Address.class, entityManager);

    }

    @Override
    public void createAddress(Address adres) {

        try {

            entityManager.getTransaction().begin();

            addressDao.persist(adres);

            entityManager.getTransaction().commit();

        } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");

            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {

            // Always clear the persistence context to prevent increasing memory ????
            entityManager.close();

        }

    }

    @Override
    public void deleteAddress(Address adres) {
        try {

            entityManager.getTransaction().begin();

            addressDao.delete(adres);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");

            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {

            // Always clear the persistence context to prevent increasing memory ????
            entityManager.close();

        }

    }

    @Override
    public void updateAddress(Address adres) {
        try {

            entityManager.getTransaction().begin();

            addressDao.update(adres);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");

            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {

            // Always clear the persistence context to prevent increasing memory ????
            entityManager.close();

        }

    }

    @Override
    public Optional<Address> findAddressByCustomerId(Long id) {

        Address adres;
        String sql = "select i from Address i  where Customer.id = :id";

        try {
            Query query = entityManager.createNamedQuery(sql);
            query.setParameter("Customer.id", id);

            adres = (Address) query.getSingleResult();

        } catch (NoResultException ex) {

            log.debug("The id for customer {} is not found in the database", id);

            return Optional.empty();

        } finally {

            entityManager.close();

        }

        return Optional.ofNullable(adres);

    }

    @Override
    public List<Address> findAllAddressByCustomerId(Long id) {

        List<Address> addresses = null;
        String sql = "select i from Address i  where Customer.id = :id";

        try {
            Query query = entityManager.createNamedQuery(sql);
            query.setParameter("Customer.id", id);

            return addresses = (List<Address>) query.getResultList();

        } catch (NoResultException ex) {
            log.debug("The id for customer {} is not found in the database", id);
            return null;
        }

    }

}
