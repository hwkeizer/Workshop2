/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer.hibernate;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.GenericDaoImpl;
import workshop2.persistencelayer.ProductService;


/**
 *
 * @author Al-Alaaq(Egelantier)
 */
public class ProductServiceHibernate extends GenericServiceHibernate implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceHibernate.class);

    @Override
    public void createProduct(Product product) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl productDao = new GenericDaoImpl(Product.class, em);
        try {
            em.getTransaction().begin();
            productDao.persist(product);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Transactie is niet uitgevoerd!");
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl productDao = new GenericDaoImpl(Product.class, em);
        try {
            em.getTransaction().begin();
            productDao.delete(em.find(Product.class, product.getId()));
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Transactie is niet uitgevoerd!");
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            em.close();
        }
    }

    @Override
    public void updateProduct(Product product) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl productDao = new GenericDaoImpl(Product.class, em);
        try {
            em.getTransaction().begin();
            productDao.update(product);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );
            // Exception doorgooien of FailedTransaction oid opgooien?
        } finally {
            em.close();
        }
    }
}
