/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import workshop1.interfacelayer.MenuActions;
import workshop1.interfacelayer.view.AccountView;
import workshop1.interfacelayer.view.MenuView;
import workshop1.interfacelayer.view.ProductView;

/**
 *
 * @author hwkei
 */
public class FrontEndController {
    private final MenuController menuController;
    private final AccountController accountController;
    private final ProductController productController;
    
    public FrontEndController() {
        menuController = new MenuController(new MenuView());
        accountController = new AccountController(new AccountView());
        productController = new ProductController(new ProductView());
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