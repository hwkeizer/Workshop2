/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.List;
import java.util.Optional;
import workshop2.domain.Customer;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import workshop2.domain.Product;
import workshop2.interfacelayer.controller.OrderController;

/**
 *
 * @author thoma
 */
public interface OrderService extends GenericService {
    
    public void createOrder(Order order, List<OrderItem> orderItemList);
    
    public void updateOrder(Order order);
    
    public void deleteOrder(Order order, List<OrderItem> orderItemList);
    
    public List<OrderItem> findAllOrderItemsAsListByOrder(Order order);
    
    public List<Order> findAllOrdersAsListByCustomer(Customer customer);
    
    public void updateProductStock(Product product);
    
}
