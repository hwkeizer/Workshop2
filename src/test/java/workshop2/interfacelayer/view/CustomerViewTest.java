/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import workshop2.interfacelayer.view.CustomerView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hwkei
 */
public class CustomerViewTest {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOutput;
    
    public CustomerViewTest() {
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
     * Test of requestFirstNameInput method, of class CustomerView.
     */
    @Test
    public void testRequestValidFirstNameInput() {
        System.out.println("testRequestValidFirstNameInput"); 
        String testInput = "Testnaam";
        CustomerView customerView = new CustomerView(new Scanner(testInput));  
        assertEquals("Valid input should return valid value", testInput, customerView.requestFirstNameInput());
    }
    
    /**
     * Test of requestFirstNameInput method, of class CustomerView.
     */
    @Test
    public void testRequestInvalidFirstNameInput() {
        System.out.println("testRequestInvalidFirstNameInput"); 
        String testInput = "\n" // Empty input is invalid
                + "Testnaam"; // End with valid input to prevent test from failing
        CustomerView customerView = new CustomerView(new Scanner(testInput));  
        customerView.requestFirstNameInput();
        assertTrue("Empty input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    }
    
    /**
     * Test of requestFirstNameInput method, of class CustomerView.
     */
    @Test
    public void testRequestAbortFirstNameInput() {
        System.out.println("testRequestAbortFirstNameInput"); 
        String testInput = "!";
        CustomerView customerView = new CustomerView(new Scanner(testInput));
        assertNull("Aborted name request should return null", customerView.requestFirstNameInput());
    }
    
// VERDERE TESTEN ZIJN NIET UITGEWERKT VANWEGE TIJDGEBREK
    /**
     * Test of requestLastNameInput method, of class CustomerView.
     */
//    @Test
//    public void testRequestLastNameInput() {
//        System.out.println("requestLastNameInput");
//        CustomerView instance = new CustomerView();
//        String expResult = "";
//        String result = instance.requestLastNameInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestPrefixInput method, of class CustomerView.
//     */
//    @Test
//    public void testRequestPrefixInput() {
//        System.out.println("requestPrefixInput");
//        CustomerView instance = new CustomerView();
//        Integer expResult = null;
//        Integer result = instance.requestPrefixInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
