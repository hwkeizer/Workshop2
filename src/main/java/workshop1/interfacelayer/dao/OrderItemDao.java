/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao;

import workshop1.domain.OrderItem;
import java.util.Optional;

/**
 *
 * @author thoma
 */
public interface OrderItemDao {
    
    void insertOrderItem(OrderItem orderItem);
    
    void updateOrderItem(OrderItem orderItem);
    
    void deleteOrderItem(OrderItem orderItem);
    
    Optional<OrderItem> findOrderItemById(int orderItemId);
    
}
