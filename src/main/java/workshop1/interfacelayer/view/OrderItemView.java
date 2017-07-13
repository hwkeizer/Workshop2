/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.Scanner;

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
     * Methods related to createOrderItem
     */
    
    
    
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
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
}
