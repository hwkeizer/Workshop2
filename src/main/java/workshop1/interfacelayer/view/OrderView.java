/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import workshop1.domain.Customer;
import workshop1.domain.Order;
import workshop1.domain.OrderItem;

/**
 *
 * @author thoma
 */
public class OrderView {
    Scanner input;
    
    public OrderView() {
        input = new Scanner(System.in);
    }
    
    OrderView(Scanner input) {
        this.input = input;
    }

    
    
    /**
     * Methods related to createOrderEmployee
     */
    
    public void showConstructOrderEmployeeStartScreen() {
        System.out.println("\n\nU gaat een nieuwe bestelling aan de database toevoegen.");
        System.out.println("Een bestelling is altijd gekoppeld aan een klant.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst\n"
                + " de klant waar u een bestelling voor wilt plaatsen.");
        input.nextLine();
    }
    

    public Optional<Order> constructOrder(Integer customerId) {
        
        //placeholde
        return null;
    }
    
    public void showOrderToBeCreated(List<OrderItem> orderItemList, Order order) {
        System.out.println("Hier onder volgt een overzicht van de bestelling:\n\n");
        
        System.out.printf("%-10s%-15s%-20s\n", "Prijs", "Datum", "Bestelstatus");
        System.out.println("---------------------------------------------");
        System.out.println(order.toStringNoId());
    }
    
    public Integer requestConfirmationToCreate() {
       printRequestForCreateConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidConfirmation(respons)) {
            showInvalidRespons();
            printRequestForCreateConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    private void printRequestForCreateConfirmation() {
        System.out.println("\nWilt u deze bestelling opslaan?");
        System.out.println("1) Bestelling opslaan");
        System.out.println("2) Bestelling NIET opslaan");
        System.out.print("> ");
    }
    
    
    
    /**
     * Methods related to createOrderCustomer
     */
    
    
    
    /**
     * Methods related to deleteOrderEmployee
     */
    
    
    
    /**
     * Methods related to createOrderEmployee
     */
    
    
    
    /**
     * Methods related to createOrderEmployee
     */
    
    
    
    /**
     * Methods related to createOrderEmployee
     */
    
    private void showInvalidRespons() {
        System.out.println("\n\n\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }

}
