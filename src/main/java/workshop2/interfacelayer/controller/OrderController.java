/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.domain.Customer;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import workshop2.domain.OrderStatus;
import static workshop2.domain.OrderStatus.*;
import workshop2.domain.Product;
import workshop2.interfacelayer.view.CustomerView;
import workshop2.interfacelayer.view.OrderItemView;
import workshop2.interfacelayer.view.OrderView;
import workshop2.persistencelayer.AccountService;
import workshop2.persistencelayer.AccountServiceFactory;
import workshop2.persistencelayer.OrderService;
import workshop2.persistencelayer.OrderServiceFactory;

/**
 *
 * @author thoma
 */
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private OrderView orderView;
    private OrderItemView orderItemView;
    private final OrderService orderService;
    
    
    OrderController(OrderView orderView, OrderItemView orderItemView) {
        this.orderView = orderView;
        this.orderItemView = orderItemView;
        orderService = OrderServiceFactory.getOrderService();
    }

    public void createOrderEmployee(CustomerController customerController) {
        orderView.showConstructOrderEmployeeStartScreen();
        Optional<Customer> optionalCustomer = customerController.selectCustomerByUser();
        Customer customer = optionalCustomer.get();
        if (customer == null) {
            // No customer selected so we skip creating the order
            return;
        }
        
        List<Product> productList = orderService.<Product>fetchAllAsList(Product.class);
        List<OrderItem> orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice(orderItemList);
        LocalDateTime dateTime = LocalDateTime.now();
        Order order = new Order(price, customer, dateTime, NIEUW); 
        
        //Get full productList, the one loaded above is emptied in the order creation process
        productList = orderService.<Product>fetchAllAsList(Product.class);
        orderView.showOrderToBeCreated(orderItemList, order, productList); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            for(OrderItem orderItem: orderItemList){
                orderItem.setOrder(order);
            }
            orderService.createOrder(order, orderItemList);

            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
    
    public void createOrderCustomer(String username) {
        orderView.showConstructOrderCustomerStartScreen();
        
        AccountService accountService = AccountServiceFactory.getAccountService();
        Account customerAccount = accountService.findAccountByUserName(username).get();
        
        CustomerView customerView = new CustomerView();
        CustomerController customerController = new CustomerController(customerView);
        Customer customer = customerController.searchCustomerByAccount(customerAccount.getId()).get();

        List<Product> productList = orderService.<Product>fetchAllAsList(Product.class);
        List<OrderItem> orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice(orderItemList);
        LocalDateTime dateTime = LocalDateTime.now();
        Order order = new Order(price, customer, dateTime, NIEUW);
        
        //Get full productList, the one loaded above is emptied in the order creation process
        productList = orderService.<Product>fetchAllAsList(Product.class);
        orderView.showOrderToBeCreated(orderItemList, order, productList); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            for(OrderItem orderItem: orderItemList){
                orderItem.setOrder(order);
            }
            orderService.createOrder(order, orderItemList);
            
            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
    
    public void showOrderToCustomer(String username) {
        orderView.showOrderListCustomerStartScreen();
        
        AccountService accountService = AccountServiceFactory.getAccountService();
        Account customerAccount = accountService.findAccountByUserName(username).get();
        
        CustomerView customerView = new CustomerView();
        CustomerController customerController = new CustomerController(customerView);
        Customer customer = customerController.searchCustomerByAccount(customerAccount.getId()).get();

        List<Order> orderList = orderService.findAllOrdersAsListByCustomer(customer);
        List<Product> productList = orderService.<Product>fetchAllAsList(Product.class);
        
        if(orderList.size() == 0) {
            orderView.showCustomerNoOrdersWereFound();
        }
        else if(orderList.size() == 1) {
            Order order = orderList.get(0);
            List<OrderItem> orderItemList = orderService.findAllOrderItemsAsListByOrder(order);
            orderView.showOneOrderWasFound();
            orderView.showOrderToCustomer(order, orderItemList, productList);
        }
        else {
            orderView.showCustomerListOfFoundOrders(orderList);
            int index = orderView.requestOrderIdToSelectFromList(orderList);
            Order order = orderList.get(index);
            List<OrderItem> orderItemList = orderService.findAllOrderItemsAsListByOrder(order);
            orderView.showCustomerThatOrderWasSelected();
            orderView.showOrderToCustomer(order, orderItemList, productList);
        }
        
        
    }
        

    public void deleteOrderEmployee(CustomerController customerController) {
        List<Order> orderList = orderService.<Order>fetchAllAsList(Order.class);
        
        List<Customer> customerList = orderService.<Customer>fetchAllAsList(Customer.class);
        
        //obtain the id of order to delete
        orderView.showDeleteOrderEmployeeStartScreen();
        orderView.showListToSelectOrderToDelete(orderList, customerList);
        Integer index = orderView.requestOrderIdToSelectFromList(orderList);
        if(index == null)
            return;
        Order selectedOrder = orderList.get(index);
        
        List<OrderItem> orderItemList = orderService.findAllOrderItemsAsListByOrder(selectedOrder);
               
        List<Product> productList = orderService.<Product>fetchAllAsList(Product.class);
        
        orderView.showOrderToBeDeleted(orderItemList, selectedOrder, customerList, productList);
        
        
        Integer confirmed = orderView.requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            orderService.deleteOrder(selectedOrder, orderItemList);
            
            //Update the stock after placing the order
            updateProductStockAfterDeletingOrder(orderItemList);
        }
    }
    
    
//    public void deleteOrderCustomer() {
//        
//    }
//
//    public void updateOrderEmployee(CustomerController customerController) {
//        
//    }
//    
//    public void updateOrderCustomer() {
//        
//    }
    
    public void setOrderStatus() {
        List<Order> orderList = orderService.<Order>fetchAllAsList(Order.class);
        
        List<Customer> customerList = orderService.<Customer>fetchAllAsList(Customer.class);
        
        //obtain the id of order to delete
        orderView.showSetOrderStatusStartScreen();
        orderView.showListToSelectOrderToSetOrderStatus(orderList, customerList);
        Integer index = orderView.requestOrderIdToSelectFromList(orderList);
        if(index == null)
            return;
        Order selectedOrder = orderList.get(index);
        
        List<OrderItem> orderItemList = orderService.findAllOrderItemsAsListByOrder(selectedOrder);
        List<Product> productList = orderService.<Product>fetchAllAsList(Product.class);
        
        orderView.showOrderToSetOrderStatus(orderItemList, selectedOrder, customerList, productList);
        Integer newOrderStatusId = orderView.requestInputForNewOrderStatus(selectedOrder);
        if(newOrderStatusId == null)
            return;
        
        OrderStatus newOrderStatus = null;
        switch(newOrderStatusId){
            case 1: newOrderStatus = NIEUW; break;
            case 2: newOrderStatus = IN_BEHANDELING; break;
            case 3: newOrderStatus = AFGEHANDELD; break;
        }

        orderView.showOrderToSetNewOrderStatusId(selectedOrder, newOrderStatusId, customerList, productList);
        
        selectedOrder.setOrderStatus(newOrderStatus);
        
        Integer confirmed = orderView.requestConfirmationToSetNewOrderStatusId();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            orderService.updateOrder(selectedOrder);
        }
    }
    
    
    protected void updateProductStockAfterCreatingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
                Optional<Product> optionalProduct = orderService.<Product>fetchById(Product.class, orderItem.getProduct().getId());
                Product product = optionalProduct.get();
                int amount = orderItem.getAmount();
                int oldStock = product.getStock();
                product.setStock(oldStock - amount);
                orderService.updateProductStock(product);
        }
    }

    protected void updateProductStockAfterDeletingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
            Optional<Product> optionalProduct = orderService.<Product>fetchById(Product.class, orderItem.getProduct().getId());
            Product product = optionalProduct.get();
            int amount = orderItem.getAmount();
            int oldStock = product.getStock();
            product.setStock(oldStock + amount);
            orderService.updateProductStock(product);
        }
    }
    
    protected BigDecimal calculateOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal price = new BigDecimal("0.00");
        for(OrderItem orderItem: orderItemList){
            price = price.add(orderItem.getSubTotal());

        }
        return price;
    }
    
    
    
    
    
}
