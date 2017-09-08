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
import workshop2.domain.Customer;
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
    public void updateOrder(Order order){
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl orderDao = new GenericDaoImpl(Order.class, em);
        try {
            em.getTransaction().begin();
            orderDao.update(order);
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
    public void deleteOrder(Order order, List<OrderItem> orderItemList){
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        GenericDaoImpl orderDao = new GenericDaoImpl(Order.class, em);
        GenericDaoImpl orderItemDao = new GenericDaoImpl(OrderItem.class, em);
        try {
            em.getTransaction().begin();
            orderDao.delete(em.find(Order.class, order.getId()));
            for(OrderItem orderItem: orderItemList){
                orderItemDao.delete(em.find(OrderItem.class, orderItem.getId()));
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
            Query query = em.createNamedQuery("findAllOrderItemsAsListByOrder");
            query.setParameter("order", order);
            orderItemList = query.getResultList();
        } catch(NoResultException ex) {
            log.debug("No OrderItems found for order: {}", order.toString());
            return null;
        } finally {
            em.close();
        }
        return orderItemList;
    }
    
    @Override
    public List<Order> findAllOrdersAsListByCustomer(Customer customer) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        List<Order> orderList;
        try {
            Query query = em.createNamedQuery("findAllOrdersAsListByCustomer");
            query.setParameter("customer", customer);
            orderList = query.getResultList();
            log.debug("List was found, list size is: " + orderList.size());
        } catch(NoResultException ex) {
            log.debug("No Orders found for customer: {}", customer.toString());
            return null;
        } finally {
            em.close();
        }
        return orderList;
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
