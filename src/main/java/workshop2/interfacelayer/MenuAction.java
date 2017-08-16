/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer;


/**
 *
 * @author hwkei
 */
public enum MenuAction {
    //Menu
    SHOW_SUBMENU, LOGOUT, PREVIOUS_SCREEN, MAIN_SCREEN,
    
    //Account
    CREATE_ACCOUNT, UPDATE_ACCOUNT, DELETE_ACCOUNT, CHANGE_OWN_PASSWORD,
    LINK_ACCOUNT_TO_CUSTOMER,
    
    //Address
    CREATE_ADDRESS, DELETE_ADDRESS, UPDATE_ADDRESS,
    
    //Customer
    CREATE_CUSTOMER, DELETE_CUSTOMER, UPDATE_CUSTOMER,
    
    //Order
    CREATE_ORDER_EMPLOYEE, CREATE_ORDER_CUSTOMER, DELETE_ORDER_EMPLOYEE,
    DELETE_ORDER_CUSTOMER, UPDATE_ORDER_EMPLOYEE, UPDATE_ORDER_CUSTOMER,
    SET_ORDER_STATUS, VIEW_ORDER_CUSTOMER,
    
    //OrderItem
    //No menu actions, methods will be accessed via the OrderController
    
    //Product
    CREATE_PRODUCT, UPDATE_PRODUCT, DELETE_PRODUCT,
    
    //Database
    SET_DATABASE_TYPE, SET_CONNECTION_POOL  
}
