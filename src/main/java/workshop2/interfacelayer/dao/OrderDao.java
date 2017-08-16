/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao;

import java.util.List;
import workshop2.domain.Order;
import java.util.Optional;

/**
 *
 * @author thoma
 */
public interface OrderDao {

    Integer insertOrder(Order order);
    
    void updateOrder(Order order);
    
    void deleteOrder(Order order);
    
    Optional<Order> findOrderById(int orderId);
    
    List<Order> getAllOrdersAsList();
    
    List<Order> getAllOrdersAsListByCustomerId(int customerId);
    
}
