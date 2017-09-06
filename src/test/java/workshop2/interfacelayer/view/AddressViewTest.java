/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import workshop2.interfacelayer.view.AddressView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop2.domain.Address;

// @author Al-Alaaq(Egelantier)
public class AddressViewTest {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOutput;
    
    public AddressViewTest() {
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
     * Test of requestStreetNameInput method, of class AddressView.
     */
    @Test
    public void testRequestValidStreetNameInput() {
        System.out.println("requestValidStreetNameInput");
        String testInput = "Teststraatnaam";
        AddressView addressView = new AddressView(new Scanner(testInput));  
        assertEquals("Valid input should return valid value", testInput, addressView.requestStreetNameInput());
    }
    
    /**
     * Test of requestStreetNameInput method, of class AddressView.
     */
    @Test
    public void testRequestInvalidStreetNameInput() {
        System.out.println("requestInvalidStreetNameInput");
        String testInput = "\n" // Empty input is invalid
                + "Teststraatnaam"; // End with valid input to prevent test from failing
        AddressView addressView = new AddressView(new Scanner(testInput));  
        addressView.requestStreetNameInput();
        assertTrue("Empty input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    }
    
    /**
     * Test of requestStreetNameInput method, of class AddressView.
     */
    @Test
    public void testRequestAbortStreetNameInput() {
        System.out.println("requestAbortStreetNameInput");
        String testInput = "!";
        AddressView addressView = new AddressView(new Scanner(testInput));  
        assertNull("Aborted name request should return null", addressView.requestStreetNameInput());
    }
    
// VERDERE TESTEN ZIJN NIET UITGEWERKT VANWEGE TIJDGEBREK
//     /**
//     * Test of requestNumberInput method, of class AddressView.
//     */
//    @Test
//    public void testRequestNumberInput() {
//        System.out.println("requestNumberInput");
//        AddressView instance = new AddressView();
//        Integer expResult = null;
//        Integer result = instance.requestNumberInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestAdditionInput method, of class AddressView.
//     */
//    @Test
//    public void testRequestAdditionInput() {
//        System.out.println("requestAdditionInput");
//        AddressView instance = new AddressView();
//        String expResult = "";
//        String result = instance.requestAdditionInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestPostalCodeInput method, of class AddressView.
//     */
//    @Test
//    public void testRequestPostalCodeInput() {
//        System.out.println("requestPostalCodeInput");
//        AddressView instance = new AddressView();
//        String expResult = "";
//        String result = instance.requestPostalCodeInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestCityInput method, of class AddressView.
//     */
//    @Test
//    public void testRequestCityInput() {
//        System.out.println("requestCityInput");
//        AddressView instance = new AddressView();
//        String expResult = "";
//        String result = instance.requestCityInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestAddressType method, of class AddressView.
//     */
//    @Test
//    public void testRequestAddressType() {
//        System.out.println("requestAddressType");
//        List<String> types = null;
//        AddressView instance = new AddressView();
//        Integer expResult = null;
//        Integer result = instance.requestAddressType(types);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of showAddressToBeConstructed method, of class AddressView.
//     */
//    @Test
//    public void testShowAddressToBeCreated() {
//        System.out.println("showAddressToBeConstructed");
//        Address address = null;
//        AddressView instance = new AddressView();
//        instance.showAddressToBeConstructed(address);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestConfirmationToCreate method, of class AddressView.
//     */
//    @Test
//    public void testRequestConfirmationToCreate() {
//        System.out.println("requestConfirmationToCreate");
//        AddressView instance = new AddressView();
//        Integer expResult = null;
//        Integer result = instance.requestConfirmationToCreate();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
