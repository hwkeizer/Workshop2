/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.view;

import java.util.Scanner;

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
    
    public String showUserNameRequest() {        
        System.out.print("\nGeef uw gebruikersnaam gevolgd door enter> ");
        return input.nextLine();
    }

    public String showUserPasswordRequest() {
        System.out.print("Geef uw wachtwoord gevolgd door enter> ");
        return input.nextLine();
    }
    
    public void showSuccesfulLogin(String userName, String userRole) {
        System.out.println("U bent ingelogd als " + userRole + " met gebruikersnaam " + userName);
    }
    
    public void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }

    public void showLogoutMessage() {
        System.out.println("\nU bent uitgelogd\n"
                + "Bedankt voor het gebruik van Applikaasie!\n");
    }

    public void showInvalidMenuChoice() {
        System.out.println("\nOngeldige waarde, druk op enter en probeer het astublieft opnieuw.\n");
        input.nextLine();
    }
    
    public void showDivider() {
        System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-");
    }

    public void showCurrentMenuHeader(String name, String userName, String userRole) {
        showDivider();
        showSuccesfulLogin(userName, userRole);
        System.out.println("U bent nu in: " + name + "\n");
        System.out.println("Kies de gewenste menu optie en druk op enter\n");
    }

    public void showUnsuccesfulLogin() {
        System.out.println("Uw gebruikersnaam en/of wachtwoord klopt niet\n");
    }

    public String showRequestForMenuChoice() {
        System.out.print("\nuw keuze> ");
        return input.nextLine();
    }
}
