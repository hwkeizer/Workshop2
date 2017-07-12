/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.interfacelayer.MenuActions;
import workshop1.interfacelayer.MenuItem;
import workshop1.interfacelayer.view.AccountView;
import workshop1.interfacelayer.view.MenuView;

/**
 *
 * @author hwkei
 */
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    final int ADMIN = 1;
    final int MEDEWERKER = 2;
    final int KLANT = 3;
    
    MenuItem currentMenu;
    
    private final MenuView menuView;
    private String userName;
    
    public MenuController(MenuView menuView) {
        this.menuView = menuView;
    }
    
    public boolean login() {
        // Show welcome and get the user credentials
        menuView.showWelcome();
        userName = menuView.requestUserName();
        if (userName == null) return false; // User initiated abort
        String password = menuView.requestPassword();
        if (password == null) return false; // User initiated abort
        
        // Validate the user credentials
        AccountController accountController = new AccountController(new AccountView());
        if (accountController.validateAccount(userName, password)) {
            int userRole = accountController.getUserRole(userName);
            switch (userRole) {
                case ADMIN : {
                    currentMenu = menuView.buildAdminMenu();
                    break;
                }
                case MEDEWERKER : {
                    currentMenu = menuView.buildEmployeeMenu();
                    break;                           
                }
                case KLANT : {
                    currentMenu = menuView.buildCustomerMenu();
                    break;
                } 
                default : {
                    log.error("Onbekende gebruikersrol gevonden bij gebruiker {}!", userName);
                    return false;
                }
            }
        } else {
            menuView.showUnsuccesfulLogin();
            return login(); // keep trying until user aborts
        } 
        return true;
    }    
    
    public MenuActions getMenuAction() {        
        while (!currentMenu.isAction())            
            switch (currentMenu.getAction()) {
                case SHOW_SUBMENU : {
                    menuView.showCurrentMenuHeader(currentMenu.getName(),userName);
                    for (MenuItem item: currentMenu.getSubMenu()) {                
                        item.printItem();
                    }
                    // get the users choice                    
                    String choice = menuView.showRequestForMenuChoice();
                    // Check all submenu's for a matching value
                    boolean found = false;
                    for (MenuItem item : currentMenu.getSubMenu()) {
                        if (item.getItemChoice().equals(choice)) {
                            currentMenu = item;
                            found = true;
                        }
                    }                    
                    if (!found) {
                        // No valid option has been made
                        menuView.showInvalidMenuChoice();
                    }
                    break;
                }
                case PREVIOUS_SCREEN : {
                    currentMenu = currentMenu.getParent().getParent();
                    break;
                }
                case MAIN_SCREEN : {
                    currentMenu = currentMenu.getMainScreen();
                    break;
                }
            }    
        MenuActions executeAction = currentMenu.getAction();
        currentMenu = currentMenu.getParent();
        return executeAction;                    
    }
    
    public void logout() {
        menuView.showLogoutMessage();
    }

    public String getLoggedInUserName() {
        return userName;
    }
    
}