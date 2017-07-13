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
        orderController = new OrderController(new OrderView());
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
                case CREATE_ORDER_EMPLOYEE : {
                    orderController.createOrderEmployee();
                    break;
                }
                case CREATE_ORDER_CUSTOMER : {
                    orderController.createOrderCustomer(menuController.getLoggedInUserName());
                    break;
                }
                case DELETE_ORDER_EMPLOYEE : {
                    orderController.deleteOrder();
                    break;
                }
                case UPDATE_ORDER_CUSTOMER : {
                    orderController.updateOrder();
                    break;
                }
                case SET_ORDER_STATUS : {
                    orderController.setOrderStatus();
                    break;
                }
                case CREATE_ADDRESS : {
                    addressController.createAddress(customerController);
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