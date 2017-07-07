/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.Scanner;

/**
 *
 * @author hwkei
 */
public class ProductView {
    Scanner input = new Scanner(System.in);
     
    public void showNewProductScreen() {
        System.out.println("\n\nU gaat een nieuw product aan de database toevoegen.\n\n"
                + "Vul de gevraagde gegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuw product afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
    }
    
    public String showNameRequest() {        
        System.out.println("Geef de naam van het product gevolgd door enter:");
        System.out.print("> ");
        return input.nextLine();
    }

    public String showPriceRequest() {
        System.out.println("\nGeef de prijs van het product gevolgd door enter:");
        System.out.print("> ");
        return input.nextLine();
    }

    public String showStockRequest() {
        System.out.println("\nGeef de voorraad van het product gevolgd door enter:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
    
}