/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer.hibernate;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.GenericDaoImpl;
import workshop2.interfacelayer.persistencelayer.ProductService;


/**
 *
 * @author Al-Alaaq(Egelantier)
 */
public class ProductServiceHibernate extends GenericServiceHibernate implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceHibernate.class);

    private EntityManager entityManager;

    private GenericDaoImpl productDao;

    public ProductServiceHibernate() {

        entityManager = DatabaseConnection.getInstance().getEntityManager();

        productDao = new GenericDaoImpl(Product.class, entityManager);

    }

    @Override
    public void createProduct(Product product) {

        try {

            entityManager.getTransaction().begin();

            productDao.persist(product);

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
    public void deleteProduct(Product product) {
        try {

            entityManager.getTransaction().begin();

            productDao.delete(product);

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
    public void updateProduct(Product product) {
        try {

            entityManager.getTransaction().begin();

            productDao.update(product);

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
    public List<Product> getAllProductsAsList() {
        List<Product> products = null;
        String sql = "Select * FROM Product ORDER BY name asc";
        Query query = entityManager.createNamedQuery(sql);
        return products = (List<Product>) query.getResultList();

    }

}
