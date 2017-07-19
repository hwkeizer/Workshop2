/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.Account;
import workshop1.domain.Address;
import workshop1.domain.Customer;
import workshop1.domain.Order;
import workshop1.domain.OrderItem;
import workshop1.domain.Product;
import workshop1.interfacelayer.dao.AccountDao;
import workshop1.interfacelayer.dao.CustomerDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.OrderDao;
import workshop1.interfacelayer.dao.OrderItemDao;
import workshop1.interfacelayer.dao.ProductDao;
import workshop1.interfacelayer.view.OrderItemView;
import workshop1.interfacelayer.view.OrderView;

/**
 *
 * @author thoma
 */
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private OrderView orderView;
    private OrderItemView orderItemView;
    private Order order;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private List<OrderItem> orderItemList;

    OrderController(OrderView orderView, OrderItemView orderItemView) {
        this.orderView = orderView;
        this.orderItemView = orderItemView;
        orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
    }

    public void createOrderEmployee(CustomerController customerController) {
        orderView.showConstructOrderEmployeeStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip creating the order
            return;
        }
        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        List<Product> productList = productDao.getAllProductsAsList();
        orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice();
        LocalDateTime dateTime = LocalDateTime.now();
        order = new Order(price, customerId, dateTime, 1); 
        
        orderView.showOrderToBeCreated(orderItemList, order); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            //insert order, retreive the database-generated orderId
            int orderId = orderDao.insertOrder(order);
            log.debug("The orderId generated by the database is " + orderId);
            
            //insert the orderItems for orderItemList after setting the orderId
            for(OrderItem orderItem: orderItemList) {
                orderItem.setOrderId(orderId);
                orderItemDao.insertOrderItem(orderItem);
            }
            
            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
    
    public void createOrderCustomer(String username) {
        orderView.showConstructOrderCustomerStartScreen();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();        
        Account customerAccount = accountDao.findAccountByUserName(username).get();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        int customerId = customerDao.findCustomerByAccountId(customerAccount.getId()).get().getId();
                        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        List<Product> productList = productDao.getAllProductsAsList();
        orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice();
        LocalDateTime dateTime = LocalDateTime.now();
        order = new Order(price, customerId, dateTime, 1); 
        
        orderView.showOrderToBeCreated(orderItemList, order); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            //insert order, retreive the database-generated orderId
            int orderId = orderDao.insertOrder(order);
            log.debug("The orderId generated by the database is " + orderId);
            
            //insert the orderItems for orderItemList after setting the orderId
            for(OrderItem orderItem: orderItemList) {
                orderItem.setOrderId(orderId);
                orderItemDao.insertOrderItem(orderItem);
            }
            
            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
        
    void updateProductStockAfterCreatingOrder(List<OrderItem> orderItemList) {
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        
        for(OrderItem orderItem: orderItemList) {
                Optional<Product> optionalProduct = productDao.findProductById(orderItem.getProductId());
                Product product = optionalProduct.get();
                int amount = orderItem.getAmount();
                int stock = product.getStock();
                product.setStock(stock - amount);
                productDao.updateProduct(product);
        }
    }

    public void deleteOrderEmployee(CustomerController customerController) {
        
    }
    
    public void deleteOrderCustomer() {
        
    }

    public void updateOrderEmployee(CustomerController customerController) {
        
    }
    
    public void updateOrderCustomer() {
        
    }
    
    public void setOrderStatus() {
        
    }
    
    private BigDecimal calculateOrderPrice() {
        BigDecimal price = new BigDecimal("0.00");
        for(OrderItem orderItem: orderItemList){
            price = price.add(orderItem.getSubTotal());

        }
        return price;
    }
    
    
    
    
    
}
