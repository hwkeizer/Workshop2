/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao.mongo;

import java.util.List;
import java.util.Optional;
import workshop1.domain.OrderItem;
import workshop1.interfacelayer.dao.OrderItemDao;

/**
 *
 * @author thoma
 */
public class OrderItemDaoMongo implements OrderItemDao {

    public OrderItemDaoMongo() {
    }

    @Override
    public void insertOrderItem(OrderItem orderItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<OrderItem> findOrderItemById(int orderItemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderItem> findAllOrderItemsAsListByOrderId(Integer orderId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
