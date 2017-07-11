/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    
    
    public void showListOfAllProducts(List<Product> productList) {
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
        System.out.println("\nU heeft aangegeven het volgende product te willen verwijderen:\n\n");
        
        System.out.printf("%-30s%-10s%-10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(product.toStringNoId());
    }
    
    public void showProductToBeUpdated(Product product){
        System.out.println("\nU heeft aangegeven het volgende product te willen wijzigen:\n\n");
        
        System.out.printf("%-30s%-10s%-10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(product.toStringNoId());
        
        System.out.println("\n\nHierna kunt u de naam, prijs of voorraad wijzigen.\n"
                + "Indien u voor een bepaald gegeven geen wijziging wenst door te voeren,\n"
                + "vul dan een sterretje in en druk op <enter>.\n");
    }
    
    public void showProductUpdateChanges(Product productBeforeUpdate, Product productAfterUpdate){
        System.out.println("\nU heeft aangegeven een volgende product te willen wijzigen:\n");
        System.out.println("Geselecteerd product voor wijzigingen:\n");
        
        System.out.printf("%-30s%-10s%-10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(productBeforeUpdate.toStringNoId());
        
        System.out.println("\nGeselecteerd product na wijzigingen:\n");
        
        System.out.printf("%-30s%-10s%-10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(productAfterUpdate.toStringNoId());
        
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
    
    public Integer requestProductIdToDeleteInput(int productListSize){
        printRequestForIdToDeleteInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(productListSize, respons)) {
            showInvalidRespons();
            printRequestForIdToDeleteInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public Integer requestConfirmationToDelete() {
        printRequestForDeleteConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForDeleteConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    public Integer requestProductIdToUpdateInput(int productListSize){
        printRequestForIdToUpdateInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(productListSize, respons)) {
            showInvalidRespons();
            printRequestForIdToUpdateInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public Integer requestConfirmationToUpdate() {
        printRequestForUpdateConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForUpdateConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    /**
     * Returns a valid product name or null if the user aborts
     * @return 
     */
    public String requestNewNameInput() {        
        printRequestForNameInput();
        String respons =  input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForNameInput();            
            respons = input.nextLine();
            if (respons.equals("*")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid product price or null if the user aborts
     * @return 
     */
    public BigDecimal requestNewPriceInput() {
        printRequestForPriceInput();
        String respons = input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isValidBigDecimal(respons)) {
            showInvalidRespons();
            printRequestForPriceInput();
            respons = input.nextLine();
            if (respons.equals("*")) return null; // User initiated abort
        }
        return new BigDecimal(respons);
    }

    /**
     * Returns a valid product stock or null if the user aborts
     * @return 
     */
    public Integer requestNewStockInput() {
        printRequestForStockInput();
        String respons = input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isValidInt(respons)) {
            showInvalidRespons();
            printRequestForStockInput();
            respons = input.nextLine();
            if (respons.equals("*")) return null;  // User initiated abort
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
    
    private void printRequestForIdToDeleteInput() {
        System.out.println("Geef het ID van het product dat u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }

    private void printRequestForDeleteConfirmation() {
        System.out.println("Wilt u dit product echt verwijderen?");
        System.out.println("1) Product verwijderen");
        System.out.println("2) Product NIET verwijderen");
        System.out.print("> ");
    }
    
    private void printRequestForIdToUpdateInput() {
        System.out.println("Geef het ID van het product dat u wilt wijzigen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForUpdateConfirmation() {
        System.out.println("\n\nWilt u deze wijziging opslaan?");
        System.out.println("1) Opslaan");
        System.out.println("2) NIET opslaan, ga terug naar menu");
        System.out.print("> ");
    }


 
}