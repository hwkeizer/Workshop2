/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import workshop2.interfacelayer.MenuAction;
import workshop2.interfacelayer.view.AccountView;
import workshop2.interfacelayer.view.AddressView;
import workshop2.interfacelayer.view.CustomerView;
import workshop2.interfacelayer.view.MenuView;
import workshop2.interfacelayer.view.OrderItemView;
import workshop2.interfacelayer.view.OrderView;
import workshop2.interfacelayer.view.ProductView;

/**
 *
 * @author hwkei
 */
public class FrontEndController {
    private final MenuController menuController;
    private AccountController accountController;
    private ProductController productController;
    private CustomerController customerController;
    private OrderController orderController;
    private AddressController addressController;
    
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
        MenuAction currentAction = menuController.getMenuAction() ;
        while (currentAction != MenuAction.LOGOUT) {
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
                case LINK_ACCOUNT_TO_CUSTOMER : {
                    customerController.linkAccountToCustomer(accountController);
                    break;
                }
                
                //Order
                case CREATE_ORDER_EMPLOYEE : {
                    orderController.createOrderEmployee(customerController);
                    break;
                }
                case CREATE_ORDER_CUSTOMER : {
                    orderController.createOrderCustomer(menuController.getLoggedInUserName());
                    break;
                }
                case VIEW_ORDER_CUSTOMER : {
                    orderController.showOrderToCustomer(menuController.getLoggedInUserName());
                    break;
                }
                case DELETE_ORDER_EMPLOYEE : {
                    orderController.deleteOrderEmployee(customerController);
                    break;
                }
                case SET_ORDER_STATUS : {
                    orderController.setOrderStatus();
                    break;
                }
                
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
                
                //Database settings
                case SET_DATABASE_TYPE : {
                    if (accountController.setDatabaseType()) {
                        // refresh the controllers with the new choosen databasetype
                        accountController = new AccountController(new AccountView());
                        productController = new ProductController(new ProductView());
                        customerController = new CustomerController(new CustomerView());
                        orderController = new OrderController(new OrderView(), new OrderItemView());
                        addressController = new AddressController(new AddressView());
                    }
                    break;
                }
                case SET_CONNECTION_POOL : {
                    accountController.setConnectionPool();
                    break;
                } 
            }
            currentAction = menuController.getMenuAction() ;
        }
        menuController.logout();
    }
}