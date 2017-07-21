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
import workshop1.domain.Product;

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
    
    public void showOrderToBeCreated(List<OrderItem> orderItemList, Order order) {
        System.out.println("\nHier onder volgt een overzicht van de bestelling:\n");
        
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
    public void showConstructOrderCustomerStartScreen() {
        System.out.println("\n\nU gaat een nieuwe bestelling plaatsen.");
        System.out.println("Druk op <enter> om verder te gaan.");
        input.nextLine();
    }
    
    
    
    /**
     * Methods related to deleteOrderEmployee
     */
    public void showDeleteOrderEmployeeStartScreen() {
        System.out.println("\n\nU gaat een bestelling uit de database verwijderen.");
        System.out.println("Hieronder volgt een lijst met alle lopende bestellingen.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst\n"
                + " de bestelling die u wenst te verwijderen.");
        input.nextLine();
    }
    
    public void showListToSelectOrderToDelete(List<Order> orderList, List<Customer> customerList) {
        int i = 1;
        System.out.printf("%-5s%-20s%10s%5s%-15s%-20s\n", "ID", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("--------------------------------------------------------------------------------");
        for(Order order: orderList) {
            int customerId = order.getCustomerId();
            String customerLastName = null;
            for(Customer customer: customerList) {
                if(customer.getId() == customerId) {
                    customerLastName = customer.getLastName();
                }
            }
            System.out.printf("%-5s%-20s%10.2f%5s%-15s%-20s\n", i, customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatusIdWord());
            i++;
        }
    }
    
    public Integer requestOrderIdToSelectFromList(List<Order> orderList){
        printRequestForOrderIdToDeleteInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(orderList.size(), respons)) {
            showInvalidRespons();
            printRequestForOrderIdToDeleteInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public void printRequestForOrderIdToDeleteInput() {        
        System.out.println("\nGeef het ID van de order die u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    public void showOrderToBeDeleted(List<OrderItem> orderItemList, Order order, List<Customer> customerList, List<Product> productList) {
        System.out.println("U heeft aangegeven de volgende bestelling te willen verwijderen:\n");
        System.out.printf("%-20s%10s%5s%-15s%-20s\n", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("-----------------------------------------------------------------");
        
        String customerLastName = null;
        int customerId = order.getCustomerId();
        for(Customer customer: customerList) {
            if(customer.getId() == customerId) {
                    customerLastName = customer.getLastName();
            }
        }
        System.out.printf("%-20s%10.2f%5s%-15s%-20s\n", customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatusIdWord());
        
        System.out.println("\nDeze bestelling bevat de volgende producten:\n");
        System.out.printf("%-30s%10s%15s\n", "Product", "Aantal", "Subtotaal");
        System.out.println("-------------------------------------------------------");

        for(OrderItem orderItem: orderItemList) {
            String productName = null;
            for(Product product: productList) {
                if(product.getId() == orderItem.getProductId())
                    productName = product.getName();
                }
            System.out.printf("%-30s%10d%15s\n", productName, orderItem.getAmount(), orderItem.getSubTotal().toString());
        }

    }
    
   public Integer requestConfirmationToDelete() {
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidConfirmation(respons)) {
            showInvalidRespons();
            printRequestForDeleteConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }


    private void printRequestForDeleteConfirmation() {
        System.out.println("\nWilt u deze bestelling echt verwijderen?");
        System.out.println("1) Bestelling verwijderen");
        System.out.println("2) Bestelling NIET verwijderen");
        System.out.print("> ");
    }
    
    
    
    
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
