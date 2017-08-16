/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.interfacelayer.dao.DaoFactory;

/**
 *
 * @author hwkei
 */
public class CustomerDaoMongoTest {
    
    public CustomerDaoMongoTest() {
    }
    
    @Before
    public void initializeDatabase() {        
        DatabaseConnection.getInstance().setDatabaseType("MONGO");
        DatabaseTest.initializeMongoDatabase();
        DatabaseTest.populateMongoDatabase();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insertCustomer method, of class CustomerDaoMongo.
     */
    @Test
    public void testInsertCustomer() {
        System.out.println("insertCustomer");
        // Get the customer Collection for easy test verification
        MongoCollection customerCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("customer");        

        //Prepare an customer to add to the database
        String testFirstName = "dinges";
        String testLastName = "haas";
        String testLastNamePrefix = "van";
        Integer testAccountId = 6;
        Customer testCustomer = new Customer(testFirstName, testLastName, testLastNamePrefix, testAccountId);
        
        // Count the records before the insert and verify the customer is not yet in the database       
        long countBefore = customerCollection.count();
        BasicDBObject query = new BasicDBObject("first_name", testFirstName);
        assertFalse("Customer should not be in database before insertion", customerCollection.find(query).iterator().hasNext());
        
        // Add the prepared customer to the database with the DAO
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        customerDao.insertCustomer(testCustomer);        
        
        // Verify the records after the insertion and verify the customer is inserted
        assertEquals("Number of customer should be increased by one.", countBefore + 1, customerCollection.count());
        assertTrue("Customer should be in database after insertion", customerCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of updateCustomer method, of class CustomerDaoMongo.
     */
    @Test
    public void testUpdateCustomer() {
        System.out.println("updateCustomer");
        
        // Get the account Collection for easy test verification
        MongoCollection customerCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("customer");
        
        // Prepare a customer to add to the database        
        Integer testId = 3;
        String testFirstName = "Jan";
        String testLastName = "Jansen";
        String testLastNamePrefix = null;
        Integer testAccountId = 3;
        Customer testCustomer = new Customer(testId, testFirstName, testLastName, testLastNamePrefix, testAccountId);
        
        // Set new username, password and customer type
        String newFirstName = "Willem";
        String newLastName = "Willemsen";
        String newLastNamePrefix = "van";
        
        testCustomer.setFirstName(newFirstName);
        testCustomer.setLastName(newLastName);
        testCustomer.setLastNamePrefix(newLastNamePrefix);
        
        // Count the number of accounts before the update and verify the old account is and the 
        // new account is not in the database
        long countBefore = customerCollection.count();
        BasicDBObject queryOld = new BasicDBObject("first_name", testFirstName);
        BasicDBObject queryNew = new BasicDBObject("first_name", newFirstName);
        assertTrue("Old Customer should be in database before update", customerCollection.find(queryOld).iterator().hasNext());
        assertFalse("New Customer should not be in database before update", customerCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        customerDao.updateCustomer(testCustomer);
                
        // Validate the old customer is not and the new customer is in the database and
        // the number of customers is the same as before
        assertFalse("Old Customer should not be in database after update", customerCollection.find(queryOld).iterator().hasNext());
        assertTrue("New Customer should be in database after update", customerCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of customers should not have changed after update", countBefore, customerCollection.count());
    }

    /**
     * Test of deleteCustomer method, of class CustomerDaoMongo.
     */
    @Test
    public void testDeleteCustomer() {
        System.out.println("deleteCustomer");
        
        // Prepare the account to be deleted   
        Integer testId = 3;
        String testFirstName = "Jan";
        String testLastName = "Jansen";
        Integer testAccountId = 3;
        Customer testAccount = new Customer(testId, testFirstName, testLastName, null, testAccountId);
        
        // Get the accountion Collection for easy test verification
        MongoCollection customerCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("customer"); 
        
        // Count the records before the deletion and verify the account is in the database       
        long countBefore = customerCollection.count();
        BasicDBObject query = new BasicDBObject("first_name", testFirstName);
        assertTrue("Customer should be in database before deletion", customerCollection.find(query).iterator().hasNext());
        
        // Delete the prepared account from the database with the DAO
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        customerDao.deleteCustomer(testAccount);        
        
        // Verify the records after the insertion and verify the account is deleted
        assertEquals("Number of accounts should be increased by one.", countBefore - 1, customerCollection.count());
        assertFalse("Account should not be in database after deletion", customerCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of findCustomerById method, of class CustomerDaoMongo.
     */
    @Test
    public void testFindExistingCustomerById() {
        System.out.println("findExistingCustomerById");
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(2, "Klaas", "Klaassen", null, 2);
        int searchId = expectedCustomer.getId();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerById(searchId);
        // Assert we found the customer and it is the customer we expected
        assertTrue("Existing customer should be present", optionalCustomer.isPresent());
        assertEquals("Existing customer should be the expected customer", expectedCustomer, optionalCustomer.get());
    }
    
    /**
     * Test of findCustomerById method, of class CustomerDaoMongo.
     */
    @Test
    public void testFindNonExistingCustomerById() {
        System.out.println("findNonExistingCustomerById");
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(20, "Klaas", "Klaassen", null, 2);
        int searchId = expectedCustomer.getId();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerById(searchId);
        
        // Assert we found the customer and it is the customer we expected
        assertFalse("Non Existing customer should not be present", optionalCustomer.isPresent());
    }

    /**
     * Test of findCustomerByAccountId method, of class CustomerDaoMongo.
     */
    @Test
    public void testFindCustomerByAccountId() {
        // TODO: moet nog geimplementeerd
    }

    /**
     * Test of findCustomerByLastName method, of class CustomerDaoMongo.
     */
    @Test
    public void testFindExistingCustomerByLastName() {
        System.out.println("findExistingCustomerByLastName");
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(2, "Klaas", "Klaassen", null, 2);
        String searchLastName = expectedCustomer.getLastName();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerByLastName(searchLastName);
        
        // Assert we found the customer and it is the customer we expected
        assertTrue("Existing customer should be present", optionalCustomer.isPresent());
        assertEquals("Existing customer should be the expected customer", expectedCustomer, optionalCustomer.get());
    }
    
    /**
     * Test of findCustomerByLastName method, of class CustomerDaoMongo.
     */
    @Test
    public void testFindNonExistingCustomerByLastName() {
        System.out.println("findNonExistingCustomerByLastName");
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(2, "Klaas", "Verkeerde naam", null, 2);
        String searchLastName = expectedCustomer.getLastName();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerByLastName(searchLastName);
        
        // Assert we found the customer and it is the customer we expected
        assertFalse("Non Existing customer should not be present", optionalCustomer.isPresent());
    }

    /**
     * Test of getAllCustomersAsList method, of class CustomerDaoMongo.
     */
    @Test
    public void testGetAllCustomersAsList() {
        System.out.println("getAllCustomersAsList");
        //declare and get the accountlist to be tested
        List<Customer> expectedCustomers = new ArrayList<>();
        List<Customer> customerList;
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        customerList = customerDao.getAllCustomersAsList();
        
        expectedCustomers.add(new Customer(1,"Piet","Pietersen","van",1));
        expectedCustomers.add(new Customer(2,"Klaas","Klaassen",null,2));
        expectedCustomers.add(new Customer(3,"Jan","Jansen",null,3));
        expectedCustomers.add(new Customer(4,"Fred","Boomsma",null,4));
        expectedCustomers.add(new Customer(5,"Joost","Snel",null,5));
        
        // Assert we found the accountList and it is the accountList we expected
        assertEquals("All Accounts should be as expected", expectedCustomers, customerList);
    }
    
}
