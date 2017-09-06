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
import org.junit.BeforeClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import workshop2.domain.Account;
import static workshop2.domain.AccountType.ADMIN;
import static workshop2.domain.AccountType.KLANT;
import static workshop2.domain.AccountType.MEDEWERKER;
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.view.CustomerView;
import workshop2.persistencelayer.CustomerService;
import workshop2.persistencelayer.CustomerServiceFactory;

/**
 *
 * @author hwkei
 */
public class CustomerControllerTest {
    CustomerView mockCustomerView;
    CustomerController customerController;
    CustomerService customerService = CustomerServiceFactory.getCustomerService();
    AccountController mockAccountController;
    static List<Customer> allCustomerList = new ArrayList<>();
    
    public CustomerControllerTest() {
    }
    
    @BeforeClass
    public static void setupDatabase() {
        // Drop account table and insert new accounts
        dropAndInsert();
    }
        
    
    @Before
    public void setupMocks() {
        mockCustomerView = mock(CustomerView.class);
        mockAccountController = mock(AccountController.class);
        customerController = new CustomerController(mockCustomerView);        
    }

    /**
     * Test of createCustomer method, of class CustomerController.
     */
    @Test
    public void testCreateCustomer() {
        // Prepare the test data
        Optional<Customer> optionalCustomer = Optional.ofNullable(new Customer("Wouter", "Willemsen", null, null));
        when(mockCustomerView.constructCustomer()).thenReturn(optionalCustomer);
        
        // Validate the creation
        assertFalse("Customer should not exist before creation", customerService.findCustomerByLastName("Willemsen").isPresent());
        customerController.createCustomer();
        assertTrue("Customer should exist after creation", customerService.findCustomerByLastName("Willemsen").isPresent());
    }

    /**
     * Test of linkAccountToCustomer method, of class CustomerController.
     */
    @Test
    public void testLinkAccountToCustomer() {
        Optional<Customer> optionalCustomer = Optional.ofNullable(new Customer("Wouter", "Wielewaal", null, null));
        Optional<Account> optionalAccount = Optional.ofNullable(new Account("wouter", "testen01", KLANT));
        when(mockCustomerView.selectCustomer(allCustomerList)).thenReturn(optionalCustomer);
        when(mockAccountController.selectAccountByUser()).thenReturn(optionalAccount);
        customerController.linkAccountToCustomer(mockAccountController);
    }

    /**
     * Test of deleteCustomer method, of class CustomerController.
     */
    @Test
    public void testDeleteCustomer() {
        System.out.println("deleteCustomer");
        CustomerController instance = null;
        instance.deleteCustomer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCustomer method, of class CustomerController.
     */
    @Test
    public void testUpdateCustomer() {
        System.out.println("updateCustomer");
        CustomerController instance = null;
        instance.updateCustomer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchCustomerByAccount method, of class CustomerController.
     */
    @Test
    public void testSearchCustomerByAccount() {
        Optional<Customer> optionalCustomer = customerController.searchCustomerByAccount(4L);
        assertTrue("SearchCustomerByAccount should return true", optionalCustomer.isPresent());
        assertEquals("Searched customer should have correct lastname", "Horst", optionalCustomer.get().getLastName());
    }

    /**
     * Test of listAllCustomers method, of class CustomerController.
     */
    @Test
    public void testListAllCustomers() {
        System.out.println("listAllCustomers");
        CustomerController instance = null;
        List<Customer> expResult = null;
        List<Customer> result = instance.listAllCustomers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectCustomerIdByUser method, of class CustomerController.
     */
    @Test
    public void testSelectCustomerIdByUser() {
        // Prepare the test data
        Optional<Customer> optionalCustomer = customerService.findCustomerByLastName("Klaassen");
        when(mockCustomerView.selectCustomer(customerService.fetchAllAsList(Customer.class))).thenReturn(optionalCustomer);
        
        // Validate the select operation
        Long resultId = customerController.selectCustomerIdByUser();
        assertEquals("SelectCustomerIdByUser should return correct ID", resultId, optionalCustomer.get().getId());
    }

    /**
     * Test of selectCustomerByUser method, of class CustomerController.
     */
    @Test
    public void testSelectCustomerByUser() {
        // Prepare the test data
        Optional<Customer> optionalCustomer = customerService.findCustomerByLastName("Pietersen");
        when(mockCustomerView.selectCustomer(customerService.fetchAllAsList(Customer.class))).thenReturn(optionalCustomer);
        
        // Validate the select operation
        Optional<Customer> resultCustomer = customerController.selectCustomerByUser();
        assertEquals("SelectCustomerByUser should return correct customer", resultCustomer.get(), optionalCustomer.get());
    }
    
    private static void dropAndInsert() {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM account").executeUpdate();
        
        // Account
        String pass1 = PasswordHash.generateHash("welkom");
        String pass2 = PasswordHash.generateHash("welkom");
        String pass3 = PasswordHash.generateHash("welkom");
        Account account1 = new Account("piet", pass1, ADMIN);
        Account account2 = new Account("klaas", pass2, MEDEWERKER);
        Account account3 = new Account("jan", pass3, KLANT);
        Account account4 = new Account("fred", pass1, KLANT);
        Account account5 = new Account("joost", pass2, KLANT);
        Account account6 = new Account("jaap", pass3, KLANT);
        Account account7 = new Account("wouter", pass1, KLANT);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);
        em.persist(account5);
        em.persist(account6);
        em.persist(account7);

        // Customer
        allCustomerList.clear();
        em.createNativeQuery("DELETE FROM customer").executeUpdate();
        Customer customer1 = new Customer("Piet", "Pietersen", null, account1);
        Customer customer2 = new Customer("Klaas", "Klaassen", "van", account2);
        Customer customer3 = new Customer("Jan", "Jansen", null, account3);
        Customer customer4 = new Customer("Fred", "Horst", "ter", account4);
        Customer customer5 = new Customer("Joost", "Draaier", "den", account5);
        Customer customer6 = new Customer("Wouter", "Wielewaal", null, null);
        allCustomerList.add(customer1);
        allCustomerList.add(customer2);
        allCustomerList.add(customer3);
        allCustomerList.add(customer4);
        allCustomerList.add(customer5);
        allCustomerList.add(customer6);
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        em.persist(customer4);
        em.persist(customer5);
        em.persist(customer6);
            
        em.getTransaction().commit();
        em.close();
    }
    
}
