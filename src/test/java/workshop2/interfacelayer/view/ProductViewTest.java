/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import workshop2.interfacelayer.view.ProductView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// @author Al-Alaaq(Egelantier)

public class ProductViewTest {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOutput;
    
    public ProductViewTest() {
    }
    
    
    /**
     * Redirect the standard output before each test
     */
    @Before
    public void setOutStream() {
        originalOutput = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
    
    /**
     * Restore the standard output to the original stream after each test
     */
    @After
    public void RestoreOutStream() {
        System.setOut(originalOutput);
        System.out.flush();
    }

    /**
     * Test of requestNameInput method, of class ProductView.
     */
    @Test
    public void testRequestValidNameInput() {
        System.out.println("testRequestValidNameInput"); 
        String testInput = "Testnaam";
        ProductView productView = new ProductView(new Scanner(testInput));  
        assertEquals("Valid input should return valid value", testInput, productView.requestNameInput());
    }
    
    /**
     * Test of requestNameInput method, of class ProductView.
     */
    @Test
    public void testRequestInvalidNameInput() {
        System.out.println("testRequestInvalidNameInput"); 
        String testInput = "\n" // First row will be empty and thus invalid
                + "Testnaam"; // End with valid input to prevent the test from failing
        ProductView productView = new ProductView(new Scanner(testInput));
        productView.requestNameInput();
        assertTrue("Empty input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    }
    
    /**
     * Test of requestNameInput method, of class ProductView.
     */
    @Test
    public void testRequestAbortNameInput() {
        System.out.println("testRequestAbortNameInput"); 
        String testInput = "!";
        ProductView productView = new ProductView(new Scanner(testInput));
        assertNull("Aborted name request should return null", productView.requestNameInput());
    }

    /**
     * Test of requestPriceInput method, of class ProductView.
     */
    @Test
    public void testRequestValidPriceInput() {
        System.out.println("requestValidPriceInput"); 
        String testInput = "12.67";
        ProductView productView = new ProductView(new Scanner(testInput));  
        assertEquals("Valid input should return valid value", new 
            BigDecimal(testInput), productView.requestPriceInput());        
    }
    
    /**
     * Test of requestPriceInput method, of class ProductView.
     */
    @Test
    public void testRequestInvalidPriceInput() {
        System.out.println("requestInvalidPriceInput"); 
        String testInput = "invalid\n" // Text is invalid price input
                + "23.67"; // End with valid input to prevent the test from failing
        ProductView productView = new ProductView(new Scanner(testInput));
        productView.requestPriceInput();
        assertTrue("Invalid input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    }
    
    /**
     * Test of requestPriceInput method, of class ProductView.
     */
    @Test
    public void testRequestAbortPriceInput() {
        System.out.println("testRequestAbortPriceInput"); 
        String testInput = "!";
        ProductView productView = new ProductView(new Scanner(testInput));
        assertNull("Aborted price request should return null", productView.requestNameInput());
    }

    /**
     * Test of requestStockInput method, of class ProductView.
     */
    @Test
    public void testRequestValidStockInput() {
        System.out.println("requestValidStockInput"); 
        String testInput = "125";
        ProductView productView = new ProductView(new Scanner(testInput));
        Integer result = Integer.parseInt(testInput);
        assertEquals("Valid input should return valid value", result, productView.requestStockInput());
    } 
    
    /**
     * Test of requestStockInput method, of class ProductView.
     */
    @Test
    public void testRequestInvalidStockInput() {
        System.out.println("requestInvalidStockInput"); 
        String testInput = "invalid\n"
                + "34";
        ProductView productView = new ProductView(new Scanner(testInput));
        productView.requestStockInput();
        assertTrue("Invalid input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    } 
    
    /**
     * Test of requestStockInput method, of class ProductView.
     */
    @Test
    public void testRequestAbortStockInput() {
        System.out.println("requestAbortStockInput"); 
        String testInput = "!";
        ProductView productView = new ProductView(new Scanner(testInput));
        assertNull("Aborted stock request should return null", productView.requestNameInput());
    }
    
    /**
     * Test of requestProductIdToDeleteInput method of class ProductView.
     */
    @Test
    public void testRequestValidProductIdToDeleteInput(){
        System.out.println("requestValidProductIdToDeleteInput");
        int productListSize = 5;
        String testInput = "2";
        ProductView productView = new ProductView(new Scanner(testInput));
        Integer result = Integer.parseInt(testInput) - 1;
        assertEquals("Valid input should return valid value", result, productView.requestProductIdToDeleteInput(productListSize));
    }
    
    /**
     * Test of requestProductIdToDeleteInput method of class ProductView.
     */
    @Test
    public void testRequestInvalidProductIdToDeleteInput(){
        System.out.println("requestInvalidProductIdToDeleteInput");
        int productListSize = 5;
        String testInput;
        Integer result;
        
        //case testInput larger than productListSize
        testInput = "6\n"
                + "4";
        ProductView productView = new ProductView(new Scanner(testInput));
        productView.requestProductIdToDeleteInput(productListSize);
        assertTrue("Invalid input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
        
        //case testInput smaller than productListSize
        testInput = "0\n"
                + "4";
        productView = new ProductView(new Scanner(testInput));
        productView.requestProductIdToDeleteInput(productListSize);
        assertTrue("Invalid input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));    
    }
    
}
