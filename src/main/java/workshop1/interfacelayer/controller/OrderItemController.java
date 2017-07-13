/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.OrderItem;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.OrderItemDao;
import workshop1.interfacelayer.view.OrderItemView;

/**
 *
 * @author thoma
 */
public class OrderItemController {
    private static final Logger log = LoggerFactory.getLogger(OrderItemController.class);
    
    private final OrderItemView orderItemView;
    private OrderItem orderItem;
    private OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();
    
    public OrderItemController(OrderItemView orderItemView) {
        this.orderItemView = orderItemView;
    }
    
    
}
