/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import workshop2.domain.OrderItem;
import workshop2.domain.Product;

/**
 *
 * @author thoma
 */
public class OrderItemView {
    Scanner input;
    
    public OrderItemView(){
        input = new Scanner(System.in);
    }
    
    public OrderItemView(Scanner input){
        this.input = input;
    }
    
    
    
    /**
     * Methods related to createOrder
     * @param productList
     * @return 
     */
    public List<OrderItem> createOrderItemListForNewOrder(List<Product> productList) {
        List<OrderItem> orderItemList = new ArrayList<>();

        System.out.println("\n\nIn de schermen die nu volgen kunt u producten toevoegen aan de bestelling.");
        int addMore;
        
        do {
            Product product = requestProductToAddToOrder(productList);
            if (product == null) break;
            productList.remove(product);
            int amount = requestAmountOfProductToAddToOrder(product);
            BigDecimal subTotal = product.getPrice().multiply(new BigDecimal(amount));
            
            orderItemList.add(new OrderItem(null, product, amount, subTotal));
            
            //No more products, so breaking the loop to add orderItems to the orderItemList
            if(productList.isEmpty()){
                System.out.println("\n\nU heeft alle beschikbare producten reeds opgenomen in uw bestelling.\n"
                        + "U gaat nu door naar het bevestigen en opslaan van de bestelling.");
                break;
            }
            addMore = requestConformationToAddAnotherProduct();
        } while(addMore == 1);

        return orderItemList;
    }
    
    private Product requestProductToAddToOrder(List<Product> productList) {
        printRequestForProductToAddToOrder(productList);
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(productList.size(), respons)) {
            showInvalidRespons();
            printRequestForProductToAddToOrder(productList);
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        int index = Integer.parseInt(respons) -1;
        return productList.get(index);
    }
    
    private void printRequestForProductToAddToOrder(List<Product> productList) {
        System.out.println("\n\nHier onder volgt een lijst met de beschikbare producten:\n");
        
        System.out.printf("%-5s%-30s%10s%10s\n", "ID", "Naam", "Prijs", "Voorraad");
        System.out.println("-------------------------------------------------------");
        int i = 1;
        for(Product product: productList){
            String id = String.format("%-5s", i);
            System.out.println(id + product.toStringNoId());
            i++;
        }
        
        System.out.println("\nGeef het ID van het product dat u aan de bestelling toe\n"
                + " wilt voegen gevolgd door <enter>:");
        System.out.print("> ");
        
    }
    
    private Integer requestAmountOfProductToAddToOrder(Product product) {
        showSelectedProductBeforeEnteringAmount(product);
        printRequestForAmountOfProductToAddToOrder();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidStock(product.getStock(), respons)) {
            showInvalidRespons();
            printRequestForAmountOfProductToAddToOrder();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    private void showSelectedProductBeforeEnteringAmount(Product product) {
        System.out.println("\nHier onder volgt het geselecteerde product:\n");
        System.out.printf("%-30s%10s%10s\n", "Naam", "Prijs", "Voorraad");
        System.out.println("--------------------------------------------------");
        System.out.println(product.toStringNoId());
    }
    
    private void printRequestForAmountOfProductToAddToOrder() {
        System.out.println("\nGeef hier onder aan welk aantal van het product u aan de\n"
                + "bestelling toe wilt voegen gevolgd door <enter>:\n"
                + "(dit aantal mag niet groter zijn dan de voorraad)");
        System.out.print("> ");
    }
    
    private Integer requestConformationToAddAnotherProduct() {
        printrequestConformationToAddAnotherProduct();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidConfirmation(respons)) {
            showInvalidRespons();
            printrequestConformationToAddAnotherProduct();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    private void printrequestConformationToAddAnotherProduct() {
        System.out.println("\nWilt u nog een product toevoegen aan de bestelling of\n"
                + "doorgaan naar het controleren en opslaan van de bestelling?");
        System.out.println("1) Nog een product aan bestelling toevoegen");
        System.out.println("2) Doorgaan naar controleren en opslaan bestelling");
        System.out.print("> ");
    }
    
    
    
    /**
     * Methods related to deleteOrderItem
     */
    
    
    
    /**
     * Methods related to updateOrderItem
     */
    
    
    
    /**
     * General orderItemView methods not related to a specific action
     */
    
    private void showInvalidRespons() {
        System.out.println("\n\n\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }

    
}
