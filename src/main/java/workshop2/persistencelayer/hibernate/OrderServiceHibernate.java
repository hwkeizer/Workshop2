/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.GenericDaoImpl;
import workshop2.persistencelayer.OrderService;

/**
 *
 * @author thoma
 */
public class OrderServiceHibernate extends GenericServiceHibernate implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceHibernate.class);

    @Override
    public void createOrder(Order order, List<OrderItem> orderItemList) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl orderDao = new GenericDaoImpl(Order.class, em);
        GenericDaoImpl orderItemDao = new GenericDaoImpl(OrderItem.class, em);
        try {
            em.getTransaction().begin();       
            orderDao.persist(order);
            for(OrderItem orderItem: orderItemList){
                orderItemDao.persist(orderItem);
            }
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
    public List<OrderItem> findAllOrderItemsAsListByOrder(Order order) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        List<OrderItem> orderItemList;
        try {
            TypedQuery<OrderItem> query = em.createNamedQuery("findAllOrderItemsAsListByOrder", OrderItem.class);

            orderItemList = query.setParameter("oder_id", order.getId()).getResultList();
            
        } catch(NoResultException ex) {
            log.debug("No OrderItems found for order: {}", order.toString());
            return null;
        } finally {
            em.close();
        }
        return orderItemList;
    }
    
    @Override
    public void updateProductStock(Product product){
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl productDao = new GenericDaoImpl(Product.class, em);
        try {
            em.getTransaction().begin();
            productDao.update(product);
            em.getTransaction().commit();            
        } catch (Exception ex) {
            em.getTransaction().rollback();
            log.error("Fout in de transactie. De transactie is teruggedraaid: {}", ex );           
            // TODO: besluiten of we exception verder doorgooien
        } finally {
            em.close();
        }
    }
    
    
}
