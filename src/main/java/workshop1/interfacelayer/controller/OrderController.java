/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.Order;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.OrderDao;
import workshop1.interfacelayer.view.OrderView;

/**
 *
 * @author thoma
 */
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private OrderView orderView;
    private Order order;
    private final OrderDao orderDao;

    OrderController(OrderView orderView) {
        this.orderView = orderView;
        orderDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderDao();
    }

    void createOrderEmployee() {
        
    }

    void createOrderCustomer(String loggedInUserName) {
        
    }

    void deleteOrder() {
        
    }

    void updateOrder() {
        
    }
    
    void setOrderStatus() {
        
    }
    
    
    
}
