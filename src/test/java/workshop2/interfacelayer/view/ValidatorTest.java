/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import workshop2.interfacelayer.view.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hwkei
 */
public class ValidatorTest {
    
    public ValidatorTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isValidNameString method, of class Validator.
     */
    @Test
    public void testIsValidNameString() {
        System.out.println("isValidNameString");
        String input = "test string";
        boolean result = Validator.isValidNameString(input);
        assertTrue("Valid namestring should return true", result);
    }
    
    /**
     * Test of isValidNameString method, of class Validator.
     */
    @Test
    public void testIsInvalidNameString() {
        System.out.println("isInvalidNameString");
        String input = ""; // Empty string is invalid
        boolean result = Validator.isValidNameString(input);
        assertFalse("Invalid namestring should return false", result);
    }

    /**
     * Test of isValidBigDecimal method, of class Validator.
     */
    @Test
    public void testIsValidBigDecimal() {
        System.out.println("isValidBigDecimal");
        String price = "23.89";
        boolean result = Validator.isValidBigDecimal(price);
        assertTrue("Valid decimal numberstring should return true", result);
        price = "23";
        result = Validator.isValidBigDecimal(price);
        assertTrue("Valid integer numberstring should return true", result);
    }
    
    /**
     * Test of isValidBigDecimal method, of class Validator.
     */
    @Test
    public void testIsInvalidBigDecimal() {
        System.out.println("isInvalidBigDecimal");
        String price = "invalid";
        boolean result = Validator.isValidBigDecimal(price);
        assertFalse("Non numeric string should return false", result);
        price = "45,89";
        result = Validator.isValidBigDecimal(price);
        assertFalse("Wrong formatted numeric string should return false", result);
    }

    /**
     * Test of isValidInt method, of class Validator.
     */
    @Test
    public void testIsValidInt() {
        System.out.println("isValidInt");
        String stock = "124";
        boolean result = Validator.isValidInt(stock);
        assertTrue("Valid integer string should return true", result);
    }
    
    /**
     * Test of isValidInt method, of class Validator.
     */
    @Test
    public void testIsInvalidInt() {
        System.out.println("isInvalidInt");
        String stock = "124s";
        boolean result = Validator.isValidInt(stock);
        assertFalse("Invalid integer string should return false", result);
        stock = "124.67";
        result = Validator.isValidInt(stock);
        assertFalse("Decimal integer string should return false", result);
    }
    
    /**
     * Test of isValidMenuOption method, of class Validator.
     */
    @Test
    public void testIsValidChoice() {
        System.out.println("isValidMenuChoice");
        String option = "1";
        boolean result = Validator.isPositiveInteger(option);
        assertTrue("Valid menuoption string should return true", result);
    }
    
    /**
     * Test of isinvalidMenuOption method, of class Validator.
     */
    @Test
    public void testIsInvalidChoice() {
        System.out.println("isInvalidMenuChoice");
        String option = "-1";
        boolean result = Validator.isPositiveInteger(option);
        assertFalse("Negative menuoption string should return false", result);
        option = "s1";
        result = Validator.isPositiveInteger(option);
        assertFalse("Invalid formatted menuoption string should return false", result);
    }
    
    /**
     * Test of isValidListIndex method, of class Validator.
     */
    @Test
    public void testIsValidListIndex() {
        System.out.println("isValidListIndex");
        String listIndex = "4";
        int listSize = 5;
        boolean result = Validator.isValidListIndex(listSize, listIndex);
        assertTrue("Valid integer string should return true", result);
    }
    
    /**
     * Test of isInvalidListIndex method, of class Validator.
     */
    @Test
    public void testIsInvalidListIndex() {
        System.out.println("isInvalidListIndex");
        String listIndex;
        int listSize = 5;        
        
        //case listIndex larger than listSize
        listIndex = "6";
        boolean result = Validator.isValidListIndex(listSize, listIndex);
        assertFalse("Index outside listSize should return false", result);
        
        //case listIndex smaller than listSize
        listIndex = "0";
        result = Validator.isValidListIndex(listSize, listIndex);
        assertFalse("Index outside listSize should return false", result);
        
        //case listIndex not a number
        listIndex = "1AB2";
        result = Validator.isValidListIndex(listSize, listIndex);
        assertFalse("Index outside listSize should return false", result);
    }
    
    /**
     * Test of isValidConfirmation method, of class Validator.
     */
    @Test
    public void testIsValidConfirmation() {
        System.out.println("isValidConfirmation");
        String choice;
                
        choice = "1";
        boolean result = Validator.isValidConfirmation(choice);
        assertTrue("Valid integer string should return true", result);
        
        choice = "2";
        result = Validator.isValidConfirmation(choice);
        assertTrue("Valid integer string should return true", result);
    }
    
    /**
     * Test of isInvalidConfirmation method, of class Validator.
     */
    @Test
    public void testIsInvalidConfirmation() {
        System.out.println("isInvalidConfirmation");
        String choice;
             
        
        //case choice larger than listSize
        choice = "6";
        boolean result = Validator.isValidConfirmation(choice);
        assertFalse("Index outside listSize should return false", result);
        
        //case choice smaller than listSize
        choice = "0";
        result = Validator.isValidConfirmation(choice);
        assertFalse("Index outside listSize should return false", result);
        
        //case choice not a number
        choice = "1AB2";
        result = Validator.isValidConfirmation(choice);
        assertFalse("Index outside listSize should return false", result);
    }
    
    /**
     * Test of isValidPostalCode method, of class Validator.
     */
    @Test
    public void testIsValidPostalCode() {
        System.out.println("isValidPostalCode");
        String postalCode;
        
        postalCode = "3417DZ";
        assertTrue("Correct postal code should return true", Validator.isValidPostalCode(postalCode));
        postalCode = "9999XX";
        assertTrue("Correct postal code should return true", Validator.isValidPostalCode(postalCode));
        postalCode = "0000AA";
        assertTrue("Correct postal code should return true", Validator.isValidPostalCode(postalCode));
        postalCode = "678DF";
        assertFalse("Invalid postal code should return false", Validator.isValidPostalCode(postalCode));
        postalCode = "67857DF";
        assertFalse("Invalid postal code should return false", Validator.isValidPostalCode(postalCode));
        postalCode = "67GHDF";
        assertFalse("Invalid postal code should return false", Validator.isValidPostalCode(postalCode));
        
    }
    
    @Test
    public void testIsValidId() {
        System.out.println("isValidId");
        Integer id = 1;
        assertTrue("Valid id should return true", Validator.isValidId(id));
        id = 0;
        assertFalse("Id 0 should return false", Validator.isValidId(id));
        id = null;
        assertFalse("Id null should return false", Validator.isValidId(id));
        id = -1;
        assertFalse("Id -1 should return false", Validator.isValidId(id));
        
    }
    
    
    
}