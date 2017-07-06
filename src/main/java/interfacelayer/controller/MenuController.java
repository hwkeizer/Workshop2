/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.controller;

import interfacelayer.MenuActions;
import interfacelayer.MenuItem;
import interfacelayer.view.AccountView;
import interfacelayer.view.MenuView;
import util.Validator;

/**
 *
 * @author hwkei
 */
public class MenuController {
    
//    private final Scanner input = new Scanner(System.in);
    
    private final MenuView menuView;
    private String userRole;
    private String userName;
    MenuItem adminMenu = new MenuItem(null, 1, "Hoofdscherm", MenuActions.SHOW_SUBMENU);
    MenuItem employeeMenu = new MenuItem(null, 2, "Hoofdscherm", MenuActions.SHOW_SUBMENU);
    MenuItem customerMenu = new MenuItem(null, 3, "Hoofdscherm", MenuActions.SHOW_SUBMENU);
    
    
    public MenuController(MenuView menuView) {
        this.menuView = menuView;
        initMenu();
    }
    
    public void login() {
        // Show welcome and get the user credentials
        menuView.showWelcome();
        userName = getUserNameFromUser();
        if (userName == null) return;
        String password = getUserPasswordFromUser();
        if (password == null) return;
        
        // Validate the user credentials
        AccountController accountController = new AccountController(new AccountView());
        if (accountController.validateAccount(userName, password)) {
            userRole = accountController.getUserRole(userName);
        } else {
            // TODO: Maak een lus dat gebruiker opnieuw kan aanloggen in plaats van programma stop
            menuView.showUnsuccesfulLogin();
            userName = null;
            userRole = null;
            return;
        }       
        
        switch (userRole) {
            case "admin": {
                showAdminMenu();
                break;
            }
            case "medewerker": {
                showEmployeeMenu();
                break;
            }
            case "klant": {
                showCustomerMenu();
                break;
            }
            default: {
                // TODO: Exceptie gooien of anders oplossen
                System.out.println("ERROR: geen rol kunnen bepalen");
            }
        }        
    }
    
    public void logout() {
        menuView.showLogoutMessage();
    }
    
    public void showEmployeeMenu() {
        
        MenuItem currentMenu = employeeMenu;
        while (currentMenu != null) {
            switch (currentMenu.getAction()) {
                case SHOW_SUBMENU : {
                    menuView.showCurrentMenuHeader(currentMenu.getName(),userName, userRole);                    
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
                case MAIN_SCREEN_EMPLOYEE : {
                    currentMenu = employeeMenu;
                    break;
                }
                case LOGOUT : {
                    logout();
                    currentMenu = null;
                    break;
                }                    
                case CREATE_PRODUCT_BY_EMPLOYEE : {
                    ProductController productController = new ProductController();
                    productController.createProduct();
                    currentMenu = currentMenu.getParent();
                    break;
                }
                case UPDATE_PRODUCT_BY_EMPLOYEE : {
                    ProductController productController = new ProductController();
                    productController.updateProduct();
                    currentMenu = currentMenu.getParent();
                    break;
                }
                case DELETE_PRODUCT_BY_EMPLOYEE : {
                    ProductController productController = new ProductController();
                    productController.deleteProduct();
                    currentMenu = currentMenu.getParent();
                    break;
                }
            }
        }
    }
    
    public void showAdminMenu() {
        
    }
    
    public void showCustomerMenu() {
        
    }
    
    private String getUserNameFromUser() {        
        String name = menuView.showUserNameRequest();
        if (name.equals("!")) return null; // User interuption
        while (!Validator.isValidNameString(name)) {
            menuView.showInvalidRespons();            
            name = menuView.showUserNameRequest();
            if (name.equals("!")) return null; // User interuption
        }
        return name;
    }
    
    private String getUserPasswordFromUser() {
        String password = menuView.showUserPasswordRequest();
        if (password.equals("!")) return null; // User interuption
        while (!Validator.isValidNameString(password)) {
            menuView.showInvalidRespons();            
            password = menuView.showUserPasswordRequest();
            if (password.equals("!")) return null; // User interuption
        }
        return password;
    }
    
    private void initMenu() {        
        MenuItem employeeProduct = new MenuItem(employeeMenu, 21, "Menu Producten", MenuActions.SHOW_SUBMENU);
        MenuItem employeeOrder = new MenuItem(employeeMenu, 22, "Menu bestellingen", MenuActions.SHOW_SUBMENU);        
        MenuItem employeeCustomer = new MenuItem(employeeMenu, 23, "Menu klanten", MenuActions.SHOW_SUBMENU);        
        MenuItem employeeLogout = new MenuItem(employeeMenu, 20, "Uitloggen", MenuActions.LOGOUT);
        employeeMenu.addSubMenu(employeeProduct);
        employeeMenu.addSubMenu(employeeOrder);
        employeeMenu.addSubMenu(employeeCustomer);
        employeeMenu.addSubMenu(employeeLogout);
        
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 211, "Toevoegen product", MenuActions.CREATE_PRODUCT_BY_EMPLOYEE));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 212, "Wijzigen product", MenuActions.UPDATE_PRODUCT_BY_EMPLOYEE));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 213, "Verwijderen product", MenuActions.DELETE_PRODUCT_BY_EMPLOYEE));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 219, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 210, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE));
        
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 229, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN));
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 220, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE));
        
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 239, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN));
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 230, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE));        


    }
}