/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.math.BigDecimal;
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
    
    public void showDuplicateProductError() {
        System.out.println("\nFout: U probeert een product toe te voegen dat al bestaat in de database.\n"
                + "Als u het bestaande product wilt wijzigen kies dan voor 'Wijzigen product'\n"
                + "Druk op <enter> om door te gaan>");
        input.nextLine();
    }
    
    /**
     * Returns a valid product name or null if the user aborts
     * @return 
     */
    public String requestNameInput() {        
        printRequestForNameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForNameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid product price or null if the user aborts
     * @return 
     */
    public BigDecimal requestPriceInput() {
        printRequestForPriceInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidBigDecimal(respons)) {
            showInvalidRespons();
            printRequestForPriceInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return new BigDecimal(respons);
    }

    /**
     * Returns a valid product stock or null if the user aborts
     * @return 
     */
    public Integer requestStockInput() {
        printRequestForStockInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons)) {
            printRequestForStockInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        return Integer.parseInt(respons);
    }

    private void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
    
    private void printRequestForNameInput() {
        System.out.println("Geef de naam van het product gevolgd door <enter>:");
        System.out.print("> ");
    } 
    
    private void printRequestForPriceInput() {
        System.out.println("Geef de prijs van het product gevolgd door <enter>:");
        System.out.print("> ");
    } 
    
    private void printRequestForStockInput() {
        System.out.println("Geef de voorraad van het product gevolgd door <enter>:");
        System.out.print("> ");
    }    
}