/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.math.BigDecimal;

/**
 *
 * @author hwkei
 */
public class Validator {
    
    /**
     * Validates if the given string is a correct name string.
     * A correct name string is:
     * - not empty 
     * @param input
     * @return 
     */
    public static boolean isValidNameString(String input) {
        return !input.isEmpty();
    }

    /**
     * Validates if the given string can be converted to a BigDecimal.
     * @param price
     * @return 
     */
    public static boolean isValidBigDecimal(String price) {
        if(price.matches("\\d{0,10}[.]\\d{0,2}") || price.matches("\\d{0,10}"))
            return true;
        else
            return false;
    }

    /**
     * Validates if the given string is a valid Integer
     * @param stock
     * @return 
     */
    public static boolean isValidInt(String stock) {
        try {
            Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Validates if the given string is a valid positive int
     * A valid positive int is:
     * - non-negative Integer 
     * @param option
     * @return 
     */
    public static boolean isPositiveInteger(String option) {
        Integer intOption;
        try {
            intOption = Integer.parseInt(option);
        } catch (NumberFormatException e) {
            return false;
        }
        return (intOption >= 0);
    }
    
   
    public static boolean isValidId(Integer id) {
        if (id == null) return false;
        return (id > 0);
    }
    
    public static boolean isValidId(Long id) {
        if (id == null) return false;
        return (id > 0);
    }
    
    public static boolean isValidListIndex(int listSize, String respons) {
        int responsInt;
        try {
            responsInt = Integer.parseInt(respons);
        } catch (NumberFormatException e) {
            return false;
        }
        
        if(responsInt <= listSize && responsInt >= 1){
            return true;
        }
        else {
            return false;
        }
    }
    
    public static boolean isValidConfirmation(String respons) {
        int responsInt;
        try {
            responsInt = Integer.parseInt(respons);
        } catch (NumberFormatException e) {
            return false;
        }
        
        if(responsInt <= 2 && responsInt >= 1)
            return true;
        else
            return false;
    }
    
    public static boolean isValidStock(int stock, String respons) {
        int responsInt;
        try{
            responsInt = Integer.parseInt(respons);
        }
        catch (NumberFormatException e) {
            return false;
        }
        
        if(responsInt >= 0 && responsInt <= stock)
            return true;
        else
            return false;
    }
    
    /**
     * Validates if the given string is a valid password
     * @param password
     * @return 
     */
    public static boolean isValidPassword(String password) {
        //TODO: moet nog geimplementeerd worden
        return true;
    }
    
    /**
     * Validates if the given string is a valid Dutch postal code
     * @param postalCode
     * @return 
     */
    public static boolean isValidPostalCode(String postalCode) {
        return postalCode.matches("^[0-9]{4}\\ ?[a-zA-Z]{2}$");
    }

    static boolean isValidOrderStatusId(String respons) {
        int responsInt;
        try{
            responsInt = Integer.parseInt(respons);
        }
        catch (NumberFormatException e) {
            return false;
        }
        
        if(responsInt >= 1 && responsInt <= 3)
            return true;
        else
            return false;
    }    
}
