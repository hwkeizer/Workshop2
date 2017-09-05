/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import workshop2.domain.Account;
import workshop2.domain.AccountType;
import static workshop2.domain.AccountType.ADMIN;
import static workshop2.domain.AccountType.KLANT;
import static workshop2.domain.AccountType.MEDEWERKER;
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.view.AccountView;
import workshop2.persistencelayer.AccountService;
import workshop2.persistencelayer.AccountServiceFactory;

/**
 *
 * @author hwkei
 */
public class AccountControllerTest {
    AccountView mockAccountView;
    AccountController accountController;
    AccountService accountService = AccountServiceFactory.getAccountService();;
    List<Account> allAccountList = new ArrayList<>();
    
    public AccountControllerTest() {
    }
    
    @Before
    public void setupMocks() {
        mockAccountView = mock(AccountView.class);
        accountController = new AccountController(mockAccountView);
        
        // Drop account table and insert new accounts
        dropAndInsert();
    }

    /**
     * Test of createAccount method, of class AccountController.
     */
    @Test
    public void testCreateAccount() {
        System.out.println("createAccount");
        
        // Prepare test values
        String testUserName = "Testnaam";
        String testPassword = "testen01";
        AccountType testAccountType = AccountType.KLANT;
        when(mockAccountView.requestUsernameInput()).thenReturn(testUserName);
        when(mockAccountView.requestPasswordInput()).thenReturn(testPassword);
        when(mockAccountView.requestAccountType()).thenReturn(testAccountType);
        
        // Validate user does not exist before creation
        Optional<Account> optionalAccount = accountService.findAccountByUserName(testUserName);
        assertFalse("Account does not exist before creation", optionalAccount.isPresent());
        
        // Create the account
        accountController.createAccount();
        
        // Find the created user back and validate
        optionalAccount = accountService.findAccountByUserName(testUserName);
        assertTrue("Account does exist after creation", optionalAccount.isPresent());
        assertEquals("Created account has correct username", testUserName, optionalAccount.get().getUsername());
        assertTrue("Created account has valid password", PasswordHash.validatePassword(testPassword, optionalAccount.get().getPassword()));
        assertEquals("Created account has correct account type", testAccountType, optionalAccount.get().getAccountType());         
    }

    /**
     * Test of updateAccount method, of class AccountController.
     */
    @Test
    public void testUpdateAccount() {
        // Prepare update test values
        String testUserName = "fred";
        String updateUserName = "freddie";
        String updatePassword = "welkomTest";
        AccountType updateAccountType = MEDEWERKER;
        Optional<Account> optionalAccount = accountService.findAccountByUserName(testUserName);
        Long testId = optionalAccount.get().getId();
        when(mockAccountView.requestAccountIdToUpdateInput(6)).thenReturn(3L);
        when(mockAccountView.requestUpdateUsernameInput()).thenReturn(updateUserName);
        when(mockAccountView.requestUpdatePasswordInput()).thenReturn(updatePassword);
        when(mockAccountView.requestUpdateAccountType()).thenReturn(updateAccountType);
        when(mockAccountView.requestConfirmationToUpdate()).thenReturn(1);
        
        assertTrue("Account should exist before update test", optionalAccount.isPresent());
        
        // Update the account
        accountController.updateAccount();
        
        // Validate the updated values    

        Optional<Account> optAccount = accountService.<Account>fetchById(Account.class, testId);
        assertTrue("Same ID should exist after update test", optAccount.isPresent());
        Account resultAccount = optAccount.get();
        assertEquals("Username should equal the updated username", updateUserName, resultAccount.getUsername());
        assertTrue("Updated password should validate correctly", PasswordHash.validatePassword(updatePassword, resultAccount.getPassword()));
        assertEquals("AccountType should equal the updated accountType", updateAccountType, resultAccount.getAccountType());        
    }

    /**
     * Test of changeOwnPassword method, of class AccountController.
     */
    @Test
    public void testChangeOwnPassword() {
        // Prepare password data values
        String userName = "joost";
        String oldPassword = "welkom";
        String newPassword = "welkom01";
        when(mockAccountView.requestOldPasswordInput()).thenReturn(oldPassword);
        when(mockAccountView.requestNewPasswordInput()).thenReturn(newPassword);
        
        // Validate the change
        assertTrue("Old password should be valid before changing", accountController.validateAccount(userName, oldPassword));
        accountController.changeOwnPassword(userName);
        assertFalse("Old password should be invalid after changing", accountController.validateAccount(userName, oldPassword));
        assertTrue("New password should be valid after changing", accountController.validateAccount(userName, newPassword));
    }

    /**
     * Test of deleteAccount method, of class AccountController.
     */
    @Test
    public void testDeleteAccount() {
        // prepare the testdata
        String userName = "joost";
        Optional<Account> account = accountService.findAccountByUserName(userName);
        when(mockAccountView.requestAccountIdInput(6)).thenReturn(4);
        when(mockAccountView.requestConfirmationToDelete(account.get())).thenReturn(1);        
        
        // Validate the deletion
        assertEquals("Account should exist before deleting", userName, account.get().getUsername());
        accountController.deleteAccount();
        assertFalse("Account should not exist after deletion", accountService.findAccountByUserName(userName).isPresent());
    }

    /**
     * Test of getUserRole method, of class AccountController.
     */
    @Test
    public void testGetUserRole() {
        List<Account> accountList = accountController.listAllAccounts();
        assertEquals("Account should have admin role", accountList.get(0).getAccountType(), ADMIN);
        assertEquals("Account should have medewerker role", accountList.get(1).getAccountType(), MEDEWERKER);
        assertEquals("Account should have klant role", accountList.get(2).getAccountType(), KLANT);
        assertEquals("Account should have klant role", accountList.get(3).getAccountType(), KLANT);
        assertEquals("Account should have klant role", accountList.get(4).getAccountType(), KLANT);
        assertEquals("Account should have klant role", accountList.get(5).getAccountType(), KLANT);
    }

    /**
     * Test of listAllAccounts method, of class AccountController.
     */
    @Test
    public void testListAllAccounts() {
        System.out.println("listAllAccounts");
        List<Account> accountList = accountController.listAllAccounts();
        // The allAccountList contains all accounts created by default
        assertEquals("Retrieving all accounts should equal the all account list" , accountList, allAccountList);
    }
    
    private void dropAndInsert() {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM account").executeUpdate();
        
        // Account
        String pass1 = PasswordHash.generateHash("welkom");
        String pass2 = PasswordHash.generateHash("welkom");
        String pass3 = PasswordHash.generateHash("welkom");
        allAccountList.clear();
        Account account1 = new Account("piet", pass1, ADMIN);
        Account account2 = new Account("klaas", pass2, MEDEWERKER);
        Account account3 = new Account("jan", pass3, KLANT);
        Account account4 = new Account("fred", pass1, KLANT);
        Account account5 = new Account("joost", pass2, KLANT);
        Account account6 = new Account("jaap", pass3, KLANT);
        allAccountList.add(account1);
        allAccountList.add(account2);
        allAccountList.add(account3);
        allAccountList.add(account4);
        allAccountList.add(account5);
        allAccountList.add(account6);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);
        em.persist(account5);
        em.persist(account6);

        // Customer
        em.createNativeQuery("DELETE FROM customer").executeUpdate();
        Customer customer1 = new Customer("Piet", "Pietersen", null, account1);
        Customer customer2 = new Customer("Klaas", "Klaassen", "van", account2);
        Customer customer3 = new Customer("Jan", "Jansen", null, account3);
        Customer customer4 = new Customer("Fred", "Horst", "ter", account4);
        Customer customer5 = new Customer("Joost", "Draaier", "den", account5);
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        em.persist(customer4);
        em.persist(customer5);
            
        em.getTransaction().commit();
    }
    
}
