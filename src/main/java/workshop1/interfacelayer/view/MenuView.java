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
        System.out.println("Wilt u inloggen met:\n"
                + "1) Gebruikersnaam en wachtwoord\n"
                + "2) Met uw inlogsleutel");
        System.out.print(">");
    }
    
    public void showSuccesfulLogin(String userName) {
        System.out.println("U bent ingelogd met gebruikersnaam " + userName);
    }
    
    public void showUnsuccesfulLogin() {
        System.out.println("\nUw gebruikersnaam en/of wachtwoord klopt niet!\n");
        System.out.println("Druk op <enter> en probeer het astublieft opnieuw.");
        input.nextLine();
    }
    
    public void showLogoutMessage() {
        System.out.println("\nU bent uitgelogd\n"
                + "Bedankt voor het gebruik van Applikaasie!\n");
    }
    
    public void showCurrentMenuHeader(String name, String userName) {
        showDivider();
        showSuccesfulLogin(userName);
        System.out.println("U bent nu in: " + name + "\n");
        System.out.println("Kies de gewenste menu optie en druk op <enter>\n");
    }
    
    public void showInvalidMenuChoice() {
        System.out.println("\nOngeldige waarde, druk op <enter> en probeer het astublieft opnieuw.");
        input.nextLine();
    }   
    
    public Integer requestLoginMethod() {        
        
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidConfirmation(respons)) {
            showInvalidRespons();
            printRequestLoginMethodInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return Integer.parseInt(respons);
    }
    
    public String requestLoginKey() {
        System.out.println("U heeft aangegeven in te willen loggen met uw inlogsleutel\n"
                + "Voer hieronder uw inlogsleutel in:\n"); 
        System.out.print(">");
        return input.nextLine();
    }
    
    public void showInvalidKey() {
        System.out.println("De inlogsleutel is niet correct, probeer opnieuw in te loggen\n"
                + "Druk op <enter> om door te gaan");
        input.nextLine();
    }
    
    private void  printRequestLoginMethodInput() {
        System.out.println("Wilt u inloggen met:\n"
                + "1) Gebruikersnaam en wachtwoord\n"
                + "2) Met uw inlogsleutel");
        System.out.print(">");
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
        MenuItem adminInformation = new MenuItem(adminMenu, 15, "Menu eigen gegevens", MenuActions.SHOW_SUBMENU, false);
        MenuItem adminDatabase = new MenuItem(adminMenu, 16, "Database instellingen", MenuActions.SHOW_SUBMENU, false);
        MenuItem adminLogout = new MenuItem(adminMenu, 10, "Uitloggen", MenuActions.LOGOUT, true);
        adminMenu.addSubMenu(adminProduct);
        adminMenu.addSubMenu(adminOrder);
        adminMenu.addSubMenu(adminCustomer);
        adminMenu.addSubMenu(adminAccount);
        adminMenu.addSubMenu(adminInformation);
        adminMenu.addSubMenu(adminDatabase);
        adminMenu.addSubMenu(adminLogout);
        
        adminProduct.addSubMenu(new MenuItem(adminProduct, 111, "Toevoegen product", MenuActions.CREATE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 112, "Wijzigen product", MenuActions.UPDATE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 113, "Verwijderen product", MenuActions.DELETE_PRODUCT, true));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 119, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminProduct.addSubMenu(new MenuItem(adminProduct, 110, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminOrder.addSubMenu(new MenuItem(adminOrder, 121, "Bestelling voor klant plaatsen", MenuActions.CREATE_ORDER_EMPLOYEE, true));
//        adminOrder.addSubMenu(new MenuItem(adminOrder, 122, "Bestelling voor klant aanpassen", MenuActions.UPDATE_ORDER_EMPLOYEE, true));
        adminOrder.addSubMenu(new MenuItem(adminOrder, 122, "Bestelling voor klant verwijderen", MenuActions.DELETE_ORDER_EMPLOYEE, true));
        adminOrder.addSubMenu(new MenuItem(adminOrder, 123, "Bestelstatus van een bestelling aanpassen", MenuActions.SET_ORDER_STATUS, true));
        adminOrder.addSubMenu(new MenuItem(adminOrder, 129, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminOrder.addSubMenu(new MenuItem(adminOrder, 120, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 131, "Toevoegen klant", MenuActions.CREATE_CUSTOMER, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 132, "Wijzigen klant", MenuActions.UPDATE_CUSTOMER, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 133, "Verwijderen klant", MenuActions.DELETE_CUSTOMER, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 134, "Adres toevoegen", MenuActions.CREATE_ADDRESS, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 135, "Adres verwijderen", MenuActions.DELETE_ADDRESS, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 136, "Adres aanpassen", MenuActions.UPDATE_ADDRESS, true));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 139, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminCustomer.addSubMenu(new MenuItem(adminCustomer, 130, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminAccount.addSubMenu(new MenuItem(adminAccount, 141, "Toevoegen account", MenuActions.CREATE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 142, "Wijzigen account", MenuActions.UPDATE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 143, "Verwijderen account", MenuActions.DELETE_ACCOUNT, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 144, "Koppel account aan klant", MenuActions.LINK_ACCOUNT_TO_CUSTOMER, true));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 149, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminAccount.addSubMenu(new MenuItem(adminAccount, 140, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminInformation.addSubMenu(new MenuItem(adminInformation, 151, "Wachtwoord wijzigen", MenuActions.CHANGE_OWN_PASSWORD, true));
        adminInformation.addSubMenu(new MenuItem(adminInformation, 159, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminInformation.addSubMenu(new MenuItem(adminInformation, 150, "Naar Hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        adminDatabase.addSubMenu(new MenuItem(adminDatabase, 161, "Wijzigen database type", MenuActions.SET_DATABASE_TYPE, true));
        adminDatabase.addSubMenu(new MenuItem(adminDatabase, 162, "Aan/uit zetten connectie pool", MenuActions.SET_CONNECTION_POOL, true));
        adminDatabase.addSubMenu(new MenuItem(adminDatabase, 169, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        adminDatabase.addSubMenu(new MenuItem(adminDatabase, 160, "Naar Hoofdscherm", MenuActions.MAIN_SCREEN, false));   
        
        return adminMenu;
    }

    public MenuItem buildEmployeeMenu() {
        MenuItem employeeMenu = new MenuItem(null, 2, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeProduct = new MenuItem(employeeMenu, 21, "Menu producten", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeOrder = new MenuItem(employeeMenu, 22, "Menu bestellingen", MenuActions.SHOW_SUBMENU, false);        
        MenuItem employeeCustomer = new MenuItem(employeeMenu, 23, "Menu klanten", MenuActions.SHOW_SUBMENU, false);  
        MenuItem employeeInformation = new MenuItem(employeeMenu, 24, "Menu eigen gegevens", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeLogout = new MenuItem(employeeMenu, 20, "Uitloggen", MenuActions.LOGOUT, true);
        employeeMenu.addSubMenu(employeeProduct);
        employeeMenu.addSubMenu(employeeOrder);
        employeeMenu.addSubMenu(employeeCustomer);
        employeeMenu.addSubMenu(employeeInformation);
        employeeMenu.addSubMenu(employeeLogout);
        
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 211, "Toevoegen product", MenuActions.CREATE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 212, "Wijzigen product", MenuActions.UPDATE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 213, "Verwijderen product", MenuActions.DELETE_PRODUCT, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 219, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 210, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 221, "Bestelling voor klant plaatsen", MenuActions.CREATE_ORDER_EMPLOYEE, true));
//        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 222, "Bestelling voor klant aanpassen", MenuActions.UPDATE_ORDER_EMPLOYEE, true));
        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 222, "Bestelling voor klant verwijderen", MenuActions.DELETE_ORDER_EMPLOYEE, true));
        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 223, "Bestelstatus van een bestelling aanpassen", MenuActions.SET_ORDER_STATUS, true));
        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 229, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeOrder.addSubMenu(new MenuItem(employeeOrder, 220, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 231, "Toevoegen klant", MenuActions.CREATE_CUSTOMER, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 232, "Wijzigen klant", MenuActions.UPDATE_CUSTOMER, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 233, "Verwijderen klant", MenuActions.DELETE_CUSTOMER, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 234, "Toevoegen adres", MenuActions.CREATE_ADDRESS, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 235, "Verwijderen adres", MenuActions.DELETE_ADDRESS, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 236, "Aanpassen adres", MenuActions.UPDATE_ADDRESS, true));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 239, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeCustomer.addSubMenu(new MenuItem(employeeCustomer, 230, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        employeeInformation.addSubMenu(new MenuItem(employeeInformation, 241, "Wachtwoord wijzigen", MenuActions.CHANGE_OWN_PASSWORD, true));
        employeeInformation.addSubMenu(new MenuItem(employeeInformation, 249, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeInformation.addSubMenu(new MenuItem(employeeInformation, 240, "Naar Hoofdscherm", MenuActions.MAIN_SCREEN, false));
        return employeeMenu;
    }
    
    public MenuItem buildCustomerMenu() {
        MenuItem customerMenu = new MenuItem(null, 3, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        MenuItem customerOrder = new MenuItem(customerMenu, 31, "Menu bestellingen", MenuActions.SHOW_SUBMENU, false);
        MenuItem customerInformation = new MenuItem(customerMenu, 32, "Menu eigen gegevens", MenuActions.SHOW_SUBMENU, false);
        MenuItem customerLogout = new MenuItem(customerMenu, 30, "Uitloggen", MenuActions.LOGOUT, true);
        customerMenu.addSubMenu(customerOrder);
        customerMenu.addSubMenu(customerInformation);
        customerMenu.addSubMenu(customerLogout);
        
        customerOrder.addSubMenu(new MenuItem(customerOrder, 311, "Bestelling plaatsen", MenuActions.CREATE_ORDER_CUSTOMER, true));
        customerOrder.addSubMenu(new MenuItem(customerOrder, 312, "Bestelling inzien", MenuActions.VIEW_ORDER_CUSTOMER, true));
        customerOrder.addSubMenu(new MenuItem(customerOrder, 319, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        customerOrder.addSubMenu(new MenuItem(customerOrder, 310, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        customerInformation.addSubMenu(new MenuItem(customerInformation, 321, "Wachtwoord wijzigen", MenuActions.CHANGE_OWN_PASSWORD, true));
        customerInformation.addSubMenu(new MenuItem(customerInformation, 329, "Naar vorig scherm", MenuActions.PREVIOUS_SCREEN, false));
        customerInformation.addSubMenu(new MenuItem(customerInformation, 320, "Naar hoofdscherm", MenuActions.MAIN_SCREEN, false));
        
        return customerMenu;
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
