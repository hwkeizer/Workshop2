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
        return adminMenu;
    }

    public MenuItem buildEmployeeMenu() {
        MenuItem employeeMenu = new MenuItem(null, 2, "Hoofdscherm", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeProduct = new MenuItem(employeeMenu, 21, "Menu Producten", MenuActions.SHOW_SUBMENU, false);
        MenuItem employeeOrder = new MenuItem(employeeMenu, 22, "Menu bestellingen", MenuActions.SHOW_SUBMENU, false);        
        MenuItem employeeCustomer = new MenuItem(employeeMenu, 23, "Menu klanten", MenuActions.SHOW_SUBMENU, false);        
        MenuItem employeeLogout = new MenuItem(employeeMenu, 20, "Uitloggen", MenuActions.LOGOUT, true);
        employeeMenu.addSubMenu(employeeProduct);
        employeeMenu.addSubMenu(employeeOrder);
        employeeMenu.addSubMenu(employeeCustomer);
        employeeMenu.addSubMenu(employeeLogout);
        
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 211, "Toevoegen product", MenuActions.CREATE_PRODUCT_BY_EMPLOYEE, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 212, "Wijzigen product", MenuActions.UPDATE_PRODUCT_BY_EMPLOYEE, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 213, "Verwijderen product", MenuActions.DELETE_PRODUCT_BY_EMPLOYEE, true));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 219, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeProduct.addSubMenu(new MenuItem(employeeProduct, 210, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE, false));
        
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 229, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeOrder.addSubMenu(new MenuItem(employeeProduct, 220, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE, false));
        
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 239, "Naar vorige scherm", MenuActions.PREVIOUS_SCREEN, false));
        employeeCustomer.addSubMenu(new MenuItem(employeeProduct, 230, "Naar hoofdscherm", MenuActions.MAIN_SCREEN_EMPLOYEE, false));
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
