/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import workshop1.interfacelayer.MenuActions;
import workshop1.interfacelayer.view.AccountView;
import workshop1.interfacelayer.view.AddressView;
import workshop1.interfacelayer.view.CustomerView;
import workshop1.interfacelayer.view.MenuView;
import workshop1.interfacelayer.view.OrderItemView;
import workshop1.interfacelayer.view.OrderView;
import workshop1.interfacelayer.view.ProductView;

/**
 *
 * @author hwkei
 */
public class FrontEndController {
    private final MenuController menuController;
    private final AccountController accountController;
    private final ProductController productController;
    private final CustomerController customerController;
    private final OrderController orderController;
    private final AddressController addressController;

    
    public FrontEndController() {
        menuController = new MenuController(new MenuView());
        accountController = new AccountController(new AccountView());
        productController = new ProductController(new ProductView());
        customerController = new CustomerController(new CustomerView());
        orderController = new OrderController(new OrderView(), new OrderItemView());
        addressController = new AddressController(new AddressView());
    }
    
    public void login() {
       if (menuController.login()) {
       showMenu();
       }
    }
    
    public void showMenu() {
        MenuActions currentAction = menuController.getMenuAction() ;
        while (currentAction != MenuActions.LOGOUT) {
            switch (currentAction) {                  
                
                //Account
                case CREATE_ACCOUNT : {
                    accountController.createAccount();
                    break;
                }
                case UPDATE_ACCOUNT : {
                    accountController.updateAccount();
                    break;
                }
                case DELETE_ACCOUNT : {
                    accountController.deleteAccount();
                    break;
                }
                case CHANGE_OWN_PASSWORD : {
                    accountController.changeOwnPassword(menuController.getLoggedInUserName());
                    break;
                }
                
                //Address
                case CREATE_ADDRESS : {
                    addressController.createAddress(customerController);
                    break;
                }
                case DELETE_ADDRESS : {
                    addressController.deleteAddress(customerController);
                    break;
                }
                case UPDATE_ADDRESS : {
                    addressController.updateAddress(customerController);
                    break;
                }
                
                //Customer                
                case CREATE_CUSTOMER : {
                    customerController.createCustomer();
                    break;
                }
                case DELETE_CUSTOMER : {
                    customerController.deleteCustomer();
                    break;
                }
                case UPDATE_CUSTOMER : {
                    customerController.updateCustomer();
                    break;
                }
                
                //Order
                case CREATE_ORDER_EMPLOYEE : {
                    orderController.createOrderEmployee(customerController);
                    break;
                }
                case CREATE_ORDER_CUSTOMER : {
                    orderController.createOrderCustomer();
                    break;
                }
                case DELETE_ORDER_EMPLOYEE : {
                    orderController.deleteOrderEmployee(customerController);
                    break;
                }
                case DELETE_ORDER_CUSTOMER : {
                    orderController.deleteOrderCustomer();
                    break;
                }
                case UPDATE_ORDER_EMPLOYEE : {
                    orderController.updateOrderEmployee(customerController);
                    break;
                }
                case UPDATE_ORDER_CUSTOMER : {
                    orderController.updateOrderCustomer();
                    break;
                }
                case SET_ORDER_STATUS : {
                    orderController.setOrderStatus();
                    break;
                }
                
                //OrderItem
                //No specific action, handled from OrderController
                
                
                //Product                
                case CREATE_PRODUCT : {
                    productController.createProduct();
                    break;
                }
                case UPDATE_PRODUCT : {
                    productController.updateProduct();
                    break;
                }
                case DELETE_PRODUCT : {
                    productController.deleteProduct();
                    break;
                }
 
            }
            currentAction = menuController.getMenuAction() ;
        }
        menuController.logout();
    }
    
     public void showAdminMenu() {
        
    }
    
    public void showCustomerMenu() {
    }
}