/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import workshop1.domain.Account;
import workshop1.interfacelayer.dao.AccountDao;
import workshop1.interfacelayer.dao.DuplicateAccountException;
import workshop1.interfacelayer.view.AccountView;

/**
 *
 * @author hwkei
 */
public class AccountControllerTest {
    AccountView mockAccountView;
    AccountDao mockAccountDao;
    AccountController accountController;
    
    public AccountControllerTest() {
    }
    
    @Before
    public void setupMocks() {        
        mockAccountView = mock(AccountView.class);
        mockAccountDao = mock(AccountDao.class);
        accountController = new AccountController(mockAccountView, mockAccountDao);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createAccount method, of class AccountController.
     * @throws workshop1.interfacelayer.dao.DuplicateAccountException
     */
    @Test
    public void testCreateAccount() throws DuplicateAccountException {
        when(mockAccountView.requestUsernameInput()).thenReturn("Testnaam");
        when(mockAccountView.requestPasswordInput()).thenReturn("testen01");
        when(mockAccountView.requestAccountType(accountController.getAvailableAccountTypes())).thenReturn(3);
        accountController.createAccount();
        verify(mockAccountDao).insertAccount(new Account("Testnaam", "testen01", 3));
    }
    
// DEZE TEST WERKT NOG NIET!
//    /**
//     * Test of createAccount method, of class AccountController.
//     * @throws workshop1.interfacelayer.dao.DuplicateAccountException
//     */
//    @Test  (expected = DuplicateAccountException.class)
//    public void testCreateDuplicateAccount() throws DuplicateAccountException {
//        when(mockAccountView.requestUsernameInput()).thenReturn("Testnaam");
//        when(mockAccountView.requestPasswordInput()).thenReturn("testen01");
//        Account account = new Account("Testnaam", "testen01", 3);
//        when(mockAccountView.requestAccountType(accountController.getAvailableAccountTypes())).thenReturn(3);
//        when((mockAccountDao.insertAccount(account))).thenThrow(new DuplicateAccountException());
//        accountController.createAccount();
//        verify(mockAccountDao).insertAccount(account);
//    }

// VOLGENDE TESTEN ZIJN NOG NIET VERDER UITGEWERKT
//    /**
//     * Test of changePassword method, of class AccountController.
//     */
//    @Test
//    public void testChangePassword() {
//        System.out.println("changePassword");
//        AccountController instance = null;
//        instance.changePassword();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of changeOwnPassword method, of class AccountController.
//     */
//    @Test
//    public void testChangeOwnPassword() {
//        System.out.println("changeOwnPassword");
//        String userName = "";
//        AccountController instance = null;
//        instance.changeOwnPassword(userName);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteAccount method, of class AccountController.
//     */
//    @Test
//    public void testDeleteAccount() {
//        System.out.println("deleteAccount");
//        AccountController instance = null;
//        instance.deleteAccount();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of validateAccount method, of class AccountController.
//     */
//    @Test
//    public void testValidateAccount() {
//        System.out.println("validateAccount");
//        String userName = "";
//        String password = "";
//        AccountController instance = null;
//        boolean expResult = false;
//        boolean result = instance.validateAccount(userName, password);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUserRole method, of class AccountController.
//     */
//    @Test
//    public void testGetUserRole() {
//        System.out.println("getUserRole");
//        String userName = "";
//        AccountController instance = null;
//        Integer expResult = null;
//        Integer result = instance.getUserRole(userName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getAvailableAccountTypes method, of class AccountController.
//     */
//    @Test
//    public void testGetAvailableAccountTypes() {
//        System.out.println("getAvailableAccountTypes");
//        AccountController instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.getAvailableAccountTypes();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of listAllAccounts method, of class AccountController.
//     */
//    @Test
//    public void testListAllAccounts() {
//        System.out.println("listAllAccounts");
//        AccountController instance = null;
//        List<Account> expResult = null;
//        List<Account> result = instance.listAllAccounts();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
