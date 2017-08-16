/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import workshop2.interfacelayer.view.AccountView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop2.domain.Account;

/**
 *
 * @author hwkei
 */
public class AccountViewTest {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOutput;
    
    public AccountViewTest() {
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
     * Test of requestUsernameInput method, of class AccountView.
     */
    @Test
    public void testRequestValidUsernameInput() {
        System.out.println("requestValidUsernameInput");
        String testInput = "Testnaam";
        AccountView accountView = new AccountView(new Scanner(testInput));
        assertEquals("Valid input should return valid username", testInput, accountView.requestUsernameInput());
    }
    
    /**
     * Test of requestUsernameInput method, of class AccountView.
     */
    @Test
    public void testRequestInvalidUsernameInput() {
        System.out.println("requestInvalidUsernameInput");
        String testInput = "\n" // Empty value is invalid input
                + "Testnaam"; // End with valid input to prevent the test from failing
        AccountView accountView = new AccountView(new Scanner(testInput));
        accountView.requestUsernameInput();
        assertTrue("Empty input should return an error notification", 
                outContent.toString().contains("Ongeldige waarde, probeer"
                + " het opnieuw of geef !<enter> om af te breken."));
    }
    
    /**
     * Test of requestUsernameInput method, of class AccountView.
     */
    @Test
    public void testRequestAbortUsernameInput() {
        System.out.println("requestAbortUsernameInput");
        String testInput = "!";
        AccountView accountView = new AccountView(new Scanner(testInput));
        assertNull("Aborted userName request should return null", accountView.requestUsernameInput());
    }
    
    
// TESTS HIERONDER ZIJN NOG NIET UITGEWERKT, ZIJN VERGELIJKBAAR ALS HIERBOVEN
//    /**
//     * Test of requestPasswordInput method, of class AccountView.
//     */
//    @Test
//    public void testRequestPasswordInput() {
//        System.out.println("requestPasswordInput");
//        AccountView instance = new AccountView();
//        String expResult = "";
//        String result = instance.requestPasswordInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestOldPasswordInput method, of class AccountView.
//     */
//    @Test
//    public void testRequestOldPasswordInput() {
//        System.out.println("requestOldPasswordInput");
//        AccountView instance = new AccountView();
//        String expResult = "";
//        String result = instance.requestOldPasswordInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestNewPasswordInput method, of class AccountView.
//     */
//    @Test
//    public void testRequestNewPasswordInput() {
//        System.out.println("requestNewPasswordInput");
//        AccountView instance = new AccountView();
//        String expResult = "";
//        String result = instance.requestNewPasswordInput();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestAccountType method, of class AccountView.
//     */
//    @Test
//    public void testRequestAccountType() {
//        System.out.println("requestAccountType");
//        List<String> types = null;
//        AccountView instance = new AccountView();
//        Integer expResult = null;
//        Integer result = instance.requestAccountType(types);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of showListOfAllAccounts method, of class AccountView.
//     */
//    @Test
//    public void testShowListOfAllAccounts() {
//        System.out.println("showListOfAllAccounts");
//        List<Account> accountList = null;
//        AccountView instance = new AccountView();
//        instance.showListOfAllAccounts(accountList);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of showAccountToBeDeleted method, of class AccountView.
//     */
//    @Test
//    public void testShowAccountToBeDeleted() {
//        System.out.println("showAccountToBeDeleted");
//        Account account = null;
//        AccountView instance = new AccountView();
//        instance.showAccountToBeDeleted(account);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestConfirmationToDelete method, of class AccountView.
//     */
//    @Test
//    public void testRequestConfirmationToDelete() {
//        System.out.println("requestConfirmationToDelete");
//        Account account = null;
//        AccountView instance = new AccountView();
//        Integer expResult = null;
//        Integer result = instance.requestConfirmationToDelete(account);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of requestAccountIdInput method, of class AccountView.
//     */
//    @Test
//    public void testRequestAccountIdInput() {
//        System.out.println("requestAccountIdInput");
//        int accountListSize = 0;
//        AccountView instance = new AccountView();
//        Integer expResult = null;
//        Integer result = instance.requestAccountIdInput(accountListSize);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
