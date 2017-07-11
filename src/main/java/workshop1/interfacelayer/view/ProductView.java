/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import workshop1.domain.Product;

/**
 *
 * @author hwkei
 */
public class ProductView {
    Scanner input;
    
    // Default public constructor will use System.in
    public ProductView() {
        input = new Scanner(System.in);
    }
    
    // Package private constructor can be injected with Scanner for testing
    ProductView(Scanner input) {
        this.input = input;
    }
     
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
    
    
    public void showListOfAllProducts(ArrayList<Product> productList) {
        System.out.println("\nHieronder volgt een lijst met alle producten.\n");
        System.out.printf("%-5s%-30s%-10s%-10s\n", "ID", "Naam", "Prijs", "Voorraad");
        System.out.println("-------------------------------------------------------");
        
        int i = 1;
        for(Product product: productList){
            String id = String.format("%-5s", i);
            System.out.println(id + product.toStringNoId());
            i++;
        }
        System.out.println("");
    }
    
    public void showProductToBeDeleted(Product product){
        System.out.println("\nU heeft aangegeven het volgende product te willen verwijderen uit de database:\n\n");
        
        String.format("%-30s%-10s%-10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(product.toStringNoId());
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
            showInvalidRespons();
            printRequestForStockInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        return Integer.parseInt(respons);
    }
    
    public Integer requestProductIdInput(int productListSize){
        printRequestForIdInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(productListSize, respons)) {
            showInvalidRespons();
            printRequestForIdInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
        
    }
    
    public Integer requestConfirmationToDelete(Product product) {
        printRequestForConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForConfirmation();
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
    
    private void printRequestForIdInput() {
        System.out.println("Geef het ID van het product dat u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }

    private void printRequestForConfirmation() {
        System.out.println("Wilt u dit product echt verwijderen?");
        System.out.println("1) Product verwijderen");
        System.out.println("2) Product NIET verwijderen");
        System.out.print("> ");
    }


 
}