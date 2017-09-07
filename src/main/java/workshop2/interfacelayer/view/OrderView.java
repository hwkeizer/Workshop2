/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.util.List;
import java.util.Scanner;
import workshop2.domain.Customer;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import workshop2.domain.OrderStatus;
import workshop2.domain.Product;

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
    
    public void showOrderToBeCreated(List<OrderItem> orderItemList, Order order, List<Product> productList) {
        System.out.println("\nHier onder volgt een overzicht van de bestelling:\n");
        
        System.out.printf("%-10s%-15s%-20s\n", "Prijs", "Datum", "Bestelstatus");
        System.out.println("---------------------------------------------");
        System.out.println(order.toStringNoId());
        
        System.out.println("\nDeze bestelling bevat de volgende producten:\n");
        System.out.printf("%-30s%10s%15s\n", "Product", "Aantal", "Subtotaal");
        System.out.println("-------------------------------------------------------");

        for(OrderItem orderItem: orderItemList) {
            String productName = null;
            for(Product product: productList) {
                if(product.getId().equals(orderItem.getProduct().getId()))
                    productName = product.getName();
                }
            System.out.printf("%-30s%10d%15s\n", productName, orderItem.getAmount(), orderItem.getSubTotal().toString());
        }
        
        
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
     * Methods related to showOrderListCustomer
     */
    public void showOrderListCustomerStartScreen() {
        System.out.println("\n\nIn de volgende schermen kunt u uw eigen bestellingen inzien.");
        System.out.println("Als u een bestelling wilt wijzigen, neem dan alstublieft contact op\n"
                + "met een medewerker");
        System.out.println("Druk op <enter> om verder te gaan.");
        input.nextLine();
    }
    
    public void showCustomerNoOrdersWereFound() {
        System.out.println("Er zijn geen bestellingen van u gevonden in het systeem.");
        System.out.println("Druk op <enter> om terug te keren naar het menu.");
        input.nextLine();
    }
    
    public void showOneOrderWasFound() {
        System.out.println("\nEr is één bestelling gevonden in het systeem:\n");
                
    }
    
    public void showCustomerListOfFoundOrders(List<Order> orderList) {
        System.out.println("\nEr zijn meerdere bestellingen gevonden in het systeem:\n");
        System.out.printf("%-5s%10s%5s%-15s%-20s\n", "ID", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("-------------------------------------------------------");
        int i = 1;
        for(Order order: orderList) {
            System.out.printf("%-5s%10s%5s%-15s%-20s\n", i, order.getTotalPrice().toString(), "", order.getDate().toLocalDate().toString(), order.getOrderStatus().toString());
            i++;
        }
    }
    
    public void showOrderToCustomer(Order order, List<OrderItem> orderItemList, List<Product> productList) {
        System.out.printf("%-10s%-15s%-20s\n", "Prijs", "Datum", "Bestelstatus");
        System.out.println("---------------------------------------------");
        System.out.println(order.toStringNoId());
        
        System.out.println("\nDeze bestelling bevat de volgende producten:\n");
        System.out.printf("%-30s%10s%15s\n", "Product", "Aantal", "Subtotaal");
        System.out.println("-------------------------------------------------------");

        for(OrderItem orderItem: orderItemList) {
            String productName = null;
            for(Product product: productList) {
                if(product.getId().equals(orderItem.getProduct().getId()))
                    productName = product.getName();
                }
            System.out.printf("%-30s%10d%15s\n", productName, orderItem.getAmount(), orderItem.getSubTotal().toString());
        }
        
        System.out.println("\nDruk op <enter> om terug te keren naar het menu.");
        input.nextLine();
    }
    
    public void showCustomerThatOrderWasSelected() {
        System.out.println("U heeft de volgende bestelling geselecteerd:\n");
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
            Long customerId = order.getCustomer().getId();
            String customerLastName = null;
            for(Customer customer: customerList) {
                if(customer.getId().equals(customerId)) {
                    customerLastName = customer.getLastName();
                }
            }
            System.out.printf("%-5s%-20s%10.2f%5s%-15s%-20s\n", i, customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatus().toString());
            i++;
        }
    }
    
    public Integer requestOrderIdToSelectFromList(List<Order> orderList){
        printRequestForOrderIdToSelectFromListInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(orderList.size(), respons)) {
            showInvalidRespons();
            printRequestForOrderIdToSelectFromListInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public void printRequestForOrderIdToSelectFromListInput() {        
        System.out.println("\nGeef het ID van de order die u wilt selecteren gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    public void showOrderToBeDeleted(List<OrderItem> orderItemList, Order order, List<Customer> customerList, List<Product> productList) {
        System.out.println("U heeft aangegeven de volgende bestelling te willen verwijderen:\n");
        System.out.printf("%-20s%10s%5s%-15s%-20s\n", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("-----------------------------------------------------------------");
        
        String customerLastName = null;
        Long customerId = order.getCustomer().getId();
        for(Customer customer: customerList) {
            if(customer.getId().equals(customerId)) {
                    customerLastName = customer.getLastName();
            }
        }
        System.out.printf("%-20s%10.2f%5s%-15s%-20s\n", customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatus().toString());
        
        System.out.println("\nDeze bestelling bevat de volgende producten:\n");
        System.out.printf("%-30s%10s%15s\n", "Product", "Aantal", "Subtotaal");
        System.out.println("-------------------------------------------------------");

        for(OrderItem orderItem: orderItemList) {
            String productName = null;
            for(Product product: productList) {
                if(product.getId().equals(orderItem.getProduct().getId()))
                    productName = product.getName();
                }
            System.out.printf("%-30s%10d%15s\n", productName, orderItem.getAmount(), orderItem.getSubTotal().toString());
        }

    }
    
   public Integer requestConfirmationToDelete() {
        printRequestForDeleteConfirmation();
        String respons = input.nextLine();
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
     * Methods related to setOrderStatus
     */
    public void showSetOrderStatusStartScreen() {
        System.out.println("\n\nU gaat de bestelstatus van een bestelling aanpassen.");
        System.out.println("Hieronder volgt een lijst met alle lopende bestellingen.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst\n"
                + "de bestelling waarvoor u de bestelstatus aan wilt passen.");
        input.nextLine();
    }
    
    public void showListToSelectOrderToSetOrderStatus(List<Order> orderList, List<Customer> customerList) {
        int i = 1;
        System.out.printf("%-5s%-20s%10s%5s%-15s%-20s\n", "ID", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("--------------------------------------------------------------------------------");
        for(Order order: orderList) {
            Long customerId = order.getCustomer().getId();
            String customerLastName = null;
            for(Customer customer: customerList) {
                if(customer.getId().equals(customerId)) {
                    customerLastName = customer.getLastName();
                }
            }
            System.out.printf("%-5s%-20s%10.2f%5s%-15s%-20s\n", i, customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatus().toString());
            i++;
        }
    }
    
    public void showOrderToSetOrderStatus(List<OrderItem> orderItemList, Order order, List<Customer> customerList, List<Product> productList) {
        System.out.println("U heeft aangegeven de bestelstatus van de volgende bestelling\n"
                + "aan te willen passen:\n");
        System.out.printf("%-20s%10s%5s%-15s%-20s\n", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("-----------------------------------------------------------------");
        
        String customerLastName = null;
        Long customerId = order.getCustomer().getId();
        for(Customer customer: customerList) {
            if(customer.getId().equals(customerId)) {
                    customerLastName = customer.getLastName();
            }
        }
        System.out.printf("%-20s%10.2f%5s%-15s%-20s\n", customerLastName, order.getTotalPrice(), "", order.getDate().toLocalDate().toString(), order.getOrderStatus().toString());
        
        System.out.println("\nDeze bestelling bevat de volgende producten:\n");
        System.out.printf("%-30s%10s%15s\n", "Product", "Aantal", "Subtotaal");
        System.out.println("-------------------------------------------------------");

        for(OrderItem orderItem: orderItemList) {
            String productName = null;
            for(Product product: productList) {
                if(product.getId().equals(orderItem.getProduct().getId()))
                    productName = product.getName();
                }
            System.out.printf("%-30s%10d%15s\n", productName, orderItem.getAmount(), orderItem.getSubTotal().toString());
        }

    }
    
    public Integer requestInputForNewOrderStatus(Order order){
        String respons = printRequestForNewOrderStatusId(order);
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidOrderStatusId(respons)) {
            showInvalidRespons();
            respons = printRequestForNewOrderStatusId(order);
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons);
    }
    
    private String printRequestForNewOrderStatusId(Order order) {
        OrderStatus orderStatus = order.getOrderStatus();
        int respons;
        Integer newOrderStatusId = null;
        switch(orderStatus) {
            case NIEUW: {
                System.out.println("\nHuidige bestelstatus is: Nieuw");
                System.out.println("Geef hier onder aan in welke bestelstatus u dit wilt veranderen:");
                System.out.println("1) In behandeling");
                System.out.println("2) Afgehandeld");
                System.out.print("> ");
                respons = input.nextInt();
                input.nextLine();
                switch(respons) {
                    case 1: newOrderStatusId = 2; break;
                    case 2: newOrderStatusId = 3; break;
                    default: newOrderStatusId = -1;
                    }
                break;
            }
            case IN_BEHANDELING: {
                System.out.println("\nHuidige bestelstatus is: In behandeling");
                System.out.println("Geef hier onder aan in welke bestelstatus u dit wilt veranderen:");
                System.out.println("1) Nieuw");
                System.out.println("2) Afgehandeld");
                System.out.print("> ");
                respons = input.nextInt();
                input.nextLine();
                switch(respons) {
                    case 1: newOrderStatusId = 1; break;
                    case 2: newOrderStatusId = 3; break;
                    default: newOrderStatusId = -1;
                }
                    break;
            }
            case AFGEHANDELD: {
                System.out.println("\nHuidige bestelstatus is: Afgehandeld");
                System.out.println("Geef hier onder aan in welke bestelstatus u dit wilt veranderen:");
                System.out.println("1) Nieuw");
                System.out.println("2) In behandeling");
                System.out.print("> ");
                respons = input.nextInt();
                input.nextLine();
                switch(respons) {
                    case 1: newOrderStatusId = 1; break;
                    case 2: newOrderStatusId = 2; break;
                    default: newOrderStatusId = -1;
                }
                break;
            }
                    
        }
        return newOrderStatusId.toString();
    }
    
    public void showOrderToSetNewOrderStatusId(Order selectedOrder, int newOrderStatusId, List<Customer> customerList, List<Product> productList) {
        System.out.println("U hebt de volgende wijziging aangebracht: \n");
        System.out.printf("%-20s%10s%5s%-15s%-20s\n", "Naam klant", "Prijs", "", "Besteldatum", "Bestelstatus");
        System.out.println("-----------------------------------------------------------------");
        
        String customerLastName = null;
        Long customerId = selectedOrder.getCustomer().getId();
        for(Customer customer: customerList) {
            if(customer.getId().equals(customerId)) {
                    customerLastName = customer.getLastName();
            }
        }
        System.out.printf("%-20s%10.2f%5s%-15s%-20s\n\n", customerLastName, selectedOrder.getTotalPrice(), "", selectedOrder.getDate().toLocalDate().toString(), selectedOrder.getOrderStatus().toString());
        
        String newOrderStatus = "";
        switch(newOrderStatusId) {
            case 1: newOrderStatus = "Nieuw"; break;
            case 2: newOrderStatus = "In behandeling"; break;
            case 3: newOrderStatus = "Afgehandeld"; break;
        }
        
        System.out.println("Nieuwe bestelstatus: " + newOrderStatus);

    }
    
    public Integer requestConfirmationToSetNewOrderStatusId() {
        printRequestForNewOrderStatsIdConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidConfirmation(respons)) {
            showInvalidRespons();
            printRequestForNewOrderStatsIdConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }


    private void printRequestForNewOrderStatsIdConfirmation() {
        System.out.println("\nWilt u deze wijziging in bestelstatus opslaan?");
        System.out.println("1) Wijziging bestelstatus opslaan");
        System.out.println("2) Wijziging bestelstatus  NIET opslaan");
        System.out.print("> ");
    }
    
    
    
    /**
     * Methods related to createOrderEmployee
     */
    
    private void showInvalidRespons() {
        System.out.println("\n\n\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }

    

    

}
