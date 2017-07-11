/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.Scanner;
import workshop1.interfacelayer.MenuActions;
import workshop1.interfacelayer.MenuItem;

/**
 *
 * @author hwkei
 */
public class MenuView {
    Scanner input = new Scanner(System.in);
    
    public void showWelcome() {
        System.out.println("\n\n\n");
        System.out.println("            *******************************");
        System.out.println("            ** WELKOM bij Applikaasie !! **");
        System.out.println("            *******************************");
        System.out.println("");
        System.out.println("");
        System.out.println("Geef uw gebruikersnaam en wachtwoord om in te loggen. ");
        System.out.println("Type een ! en enter om af te breken");
    }
    
    public void showSuccesfulLogin(String userName) {
        System.out.println("U bent ingelogd met gebruikersnaam " + userName);
    }
    
    public void showUnsuccesfulLogin() {
        System.out.println("Uw gebruikersnaam en/of wachtwoord klopt niet\n");
    }
    
    public void showLogoutMessage() {
        System.out.println("\nU bent uitgelogd\n"
                + "Bedankt voor het gebruik van Applikaasie!\n");
    }
    
    public void showCurrentMenuHeader(String name, String userName) {
        showDivider();
        showSuccesfulLogin(userName);
        System.out.println("U bent nu in: " + name + "\n");
        System.out.println("Kies de gewenste menu optie en druk op enter\n");
    }
    
    public void showInvalidMenuChoice() {
        System.out.println("\nOngeldige waarde, druk op enter en probeer het astublieft opnieuw.");
        input.nextLine();
    }
    
    public MenuItem buildCustomerMenu() {
        MenuItem customerMenu = new MenuItem(null, 3, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        return customerMenu;
    }    
    
    public String requestUserName() {        
        
        printRequestForUserNameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForUserNameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }

    public String requestPassword() {
        printRequestForPasswordInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    public String showRequestForMenuChoice() {
        System.out.print("\nuw keuze> ");
        String respons = input.nextLine();
        while (!Validator.isValidInt(respons)) {
            showInvalidMenuChoice();
            System.out.print("\nuw keuze> ");
            respons = input.nextLine();
        }        
        return respons;
    }

    public MenuItem buildAdminMenu() {
        MenuItem adminMenu = new MenuItem(null, 1, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        MenuItem adminProduct = new MenuItem(adminMenu, 11, "Menu producten", MenuActions.SHOW_SUBMENU, false);
        MenuItem adminOrder = new MenuItem(adminMenu, 12, "Menu bestellingen", MenuActions.SHOW_SUBMENU, false);        
        MenuItem adminCustomer = new MenuItem(adminMenu, 13, "Menu klanten", MenuActions.SHOW_SUBMENU, false); 
        MenuItem adminAccount = new MenuItem(adminMenu, 14, "Menu accounts", MenuActions.SHOW_SUBMENU, false);
        MenuItem adminLogout = new MenuItem(adminMenu, 10, "Uitloggen", MenuActions.LOGOUT, true);
        adminMenu.addSubMenu(adminProduct);
        adminMenu.addSubMenu(adminOrder);
        adminMenu.addSubMenu(adminCustomer);
        adminMenu.addSubMenu(adminAccount);
        adminMenu.addSubMenu(adminLogout);
        
        adminAccount.addSubMenu(new MenuItem(adminAccount, 141, "Toevoegen account", MenuActions.CREATE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 142, "Wijzigen account", MenuActions.UPDATE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 143, "Verwijderen account", MenuActions.DELETE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 149, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 140, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminProduct.addSubMenu(new MenuItem(adminProduct, 111, "Toevoegen product", MenuActions.CREATE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 112, "Wijzigen product", MenuActions.UPDATE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 113, "Verwijderen product", MenuActions.DELETE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 119, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 110, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        return adminMenu;
    }

    public MenuItem buildEmployeeMenu() {
        MenuItem employeeMenu = new MenuItem(null, 2, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeProduct = new MenuItem(employeeMenu, 21, "Menu producten", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeOrder = new MenuItem(employeeMenu, 22, "Menu bestellingen", MenuActions.SHOW_SUBMENU, false);        
        MenuItem employeeCustomer = new MenuItem(employeeMenu, 23, "Menu klanten", MenuActions.SHOW_SUBMENU, false);        
        MenuItem employeeLogout = new MenuItem(employeeMenu, 20, "Uitloggen", MenuActions.LOGOUT, true);
        employeeMenu.addSubMenu(employeeProduct);
        employeeMenu.addSubMenu(employeeOrder);
        employeeMenu.addSubMenu(employeeCustomer);
        employeeMenu.addSubMenu(employeeLogout);
        
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 211, "Toevoegen product", MenuActions.CREATE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 212, "Wijzigen product", MenuActions.UPDATE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 213, "Verwijderen product", MenuActions.DELETE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 219, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 210, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 229, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 220, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 239, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 230, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        return employeeMenu;
    }
    
    private void printRequestForUserNameInput() {
        System.out.print("\nGeef uw gebruikersnaam gevolgd door enter> ");
    }
    
    private void printRequestForPasswordInput() {
        System.out.print("Geef uw wachtwoord gevolgd door enter> ");
    }
    
    private void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.");
    }

    private void showDivider() {
        System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-");
    }
}
