/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thoma
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class CustomerDaoMysqlTest {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerDaoMysqlTest.class);

    private final int initialNumberOfCustomers = 5; // Initial number of customers
    
    public CustomerDaoMysqlTest() {
    }
    
    @Before
    public void initializeDatabase() {        
        DatabaseConnection.getInstance().setDatabaseType("MYSQL");
        DatabaseTest.initializeDatabase();
        DatabaseTest.populateDatabase();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of insertCustomer method, of class CustomerDaoMysql.
     */
    @Test
    public void testInsertCustomer() {
        
        System.out.println("insertCustomer");
        
        //Prepare an customer to add to the database
        String testFirstName = "dinges";
        String testLastName = "haas";
        String testLastNamePrefix = "van";
        Integer testAccountId = 6;
        Customer testCustomer = new Customer(testFirstName, testLastName, testLastNamePrefix, testAccountId);

        // Count the records before the insert
        int countBefore = getTableCount("customer");
        log.debug("count before is " + countBefore);

        // Add a test customer to the database
        //DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();        
        customerDao.insertCustomer(testCustomer);
        
        log.debug("count after inserting is " + getTableCount("customer"));
        
        // Count the records after the insert and compare with before
        assertEquals("Number of customers should be increased by one.", countBefore + 1, getTableCount("customer"));

        // Try to fetch the customer from the database. If it exists and ID is not the same as allready present in database, we have succesfully created a new customer
        final String query = "SELECT * FROM `customer` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfCustomers + " AND `first_name`=? AND `last_name`=? AND `ln_prefix`=? AND `account_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testFirstName);
            stat.setString(2,testLastName);
            stat.setString(3,testLastNamePrefix);
            stat.setInt(4,testAccountId);
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue(resultSet.next()); 
                
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals(1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }     
    }

    /**
     * Test of findExistingCustomerById method, of class CustomerDaoMysql.
     */
    @Test
    public void testFindExistingCustomerByLastName() {
        System.out.println("findExistingCustomerByLastName");
        
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(3, "Jan", "Jansen", null, 3);
        String searchString = expectedCustomer.getLastName();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerByLastName(searchString);
        
        // Assert we found the customer and it is the customer we expected
        assertTrue("Existing Customer should be found", optionalCustomer.isPresent());
        assertEquals("Existing Customer should be as expected", expectedCustomer, optionalCustomer.get());
    }
    
    /**
     * Test of findNonExistingCustomerById method, of class CustomerDaoMysql.
     */
    @Test
    public void testFindNonExistingCustomerByLastName() {
        System.out.println("findNonExistingCustomerByLastName");
        
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(3, "Jan", "Onbekend", null, 3);
        String searchString = expectedCustomer.getLastName();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerByLastName(searchString);
        
        // Assert we found the customer and it is the customer we expected
        assertFalse("Non existing Customer should be found", optionalCustomer.isPresent());
    }
    
    /**
     * Test of findExistingCustomerById method, of class CustomerDaoMysql.
     */
    @Test
    public void testFindExistingCustomerById() {
        System.out.println("findExistingCustomerById");
        
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(3, "Jan", "Jansen", null, 3);
        int searchId = expectedCustomer.getId();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerById(searchId);
        
        // Assert we found the customer and it is the customer we expected
        assertTrue("Existing Customer should be present", optionalCustomer.isPresent());
        assertEquals("Existing Customer should be as expected", expectedCustomer, optionalCustomer.get());
    }
    
    /**
     * Test of findNonExistingCustomerById method, of class CustomerDaoMysql.
     */
    @Test
    public void testFindNonExistingCustomerById() {
        System.out.println("findNonExistingCustomerById");
        
        // Define the customer to be searched
        Customer expectedCustomer = new Customer(30, "Jan", "Jansen", null, 3);
        int searchId = expectedCustomer.getId();
        
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        Optional<Customer> optionalCustomer = customerDao.findCustomerById(searchId);
        
        // Assert we found the customer and it is the customer we expected
        assertFalse("Non existing Customer should not be present", optionalCustomer.isPresent());
    }
    
    /**
     * Test of deleteCustomer method, of class CustomerDaoMysql.
     */
    @Test
    public void testDeleteCustomer() {
        System.out.println("deleteCustomer");
        
        // Prepare the customer to be deleted   
        Integer testId = 3;
        String testFirstName = "Jan";
        String testLastName = "Jansen";
        String testLastNamePrefix = null;
        Integer testAccountId = 3;
        Customer testCustomer = new Customer(testId, testFirstName, testLastName, testLastNamePrefix, testAccountId);
        
        
        // Try to fetch the customer from the database. It must exist or testing will make no sense
        // If LastNamePrefix equals null, then different query than if lastNamePrefix != null
        final String query;
        if(testLastNamePrefix == null) {
            query = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix` is null";
        }
        else {
            query = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? and `ln_prefix`=?";
        }
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, testId);
            stat.setString(2, testFirstName);
            stat.setString(3,testLastName);
            stat.setInt(4,testAccountId);
            if(testLastNamePrefix != null) {
                stat.setString(5,testLastNamePrefix);
            }
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue("Customer should exist before testing delete", resultSet.next());                 
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals("Customer should only exist once before a valid delete test can be done", 1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
        
        // Count the records bedore the deletion
        int countBefore = getTableCount("customer");
        
        // Perform the deletion of the customer
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();        
        customerDao.deleteCustomer(testCustomer);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of customers should be decreased by one.", countBefore - 1, getTableCount("customer"));
        
        // Try to fetch the customer from the database. If it does not exist we have succesfully deleted the customer
        // If LastNamePrefix equals null, then different query than if lastNamePrefix != null
        final String queryForDeleted;
        if(testLastNamePrefix == null) {
            queryForDeleted = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix` is null";
        }
        else {
            queryForDeleted = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? and `ln_prefix`=?";
        }
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setInt(1, testId);
            stat.setString(2, testFirstName);
            stat.setString(3,testLastName);
            stat.setInt(4,testAccountId);
            if(testLastNamePrefix != null) {
                stat.setString(5,testLastNamePrefix);
            }
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
    }
    
     /**
     * Helper function to get the number of records from a table
     */
    int getTableCount(String table) {

        final String countQuery = "SELECT COUNT(*) FROM `" + table + "`";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            Statement stat = connection.createStatement();
            try (ResultSet resultSet = stat.executeQuery(countQuery)) {
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }            
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return -1;
    }
    
    /**
     * Test of updateCustomer method, of class CustomerDaoMysql.
     */
    @Test
    public void testUpdateCustomer() {
        System.out.println("updateCustomer");
        
        // Prepare a customer to add to the database        
        Integer testId = 3;
        String testFirstName = "Jan";
        String testLastName = "Jansen";
        String testLastNamePrefix = null;
        Integer testAccountId = 3;
        Customer testCustomer = new Customer(testId, testFirstName, testLastName, testLastNamePrefix, testAccountId);
        
        // Set new firstname, lastname and lastname prefix
        String newFirstName = "Willem";
        String newLastName = "Willemsen";
        String newLastNamePrefix = "van";
        
        testCustomer.setFirstName(newFirstName);
        testCustomer.setLastName(newLastName);
        testCustomer.setLastNamePrefix(newLastNamePrefix);
        
        // Perform the update in the databse
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        customerDao.updateCustomer(testCustomer);
        
        // Validate the update
        final String beforeCustomerUpdateQuery;
        if(testLastNamePrefix == null){
            beforeCustomerUpdateQuery = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix` is null";
        }
        else {
            beforeCustomerUpdateQuery = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix`=?";
        }
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            // Try to find the customer with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(beforeCustomerUpdateQuery);
            stat.setInt(1,testId);
            stat.setString(2,testFirstName);
            stat.setString(3,testLastName);
            stat.setInt(4,testAccountId);
            if(testLastNamePrefix != null) {
                stat.setString(5,testLastNamePrefix);
            }
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the customer with the new values in the database. This should succeed
            final String afterCustomerUpdateQuery;
            if(newLastNamePrefix == null){
                afterCustomerUpdateQuery = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix` is null";
            }
             else {
                afterCustomerUpdateQuery = "SELECT * FROM `customer` WHERE `id`=? AND `first_name`=? AND `last_name`=? AND `account_id`=? AND `ln_prefix`=?";
            }
            
            stat = connection.prepareStatement(afterCustomerUpdateQuery);
            stat.setInt(1,testId);
            stat.setString(2,newFirstName);
            stat.setString(3,newLastName);
            stat.setInt(4,testAccountId);
            if(newLastNamePrefix != null) {
                stat.setString(5,newLastNamePrefix);
            }
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue(resultSet.next()); 
                
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals(1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }     
    }
    
    /**
     * Test of getAllCustomersAsList method, of class CustomerDaoMysql.
     */
    @Test
    public void testGetAllCustomersAsList() {
        System.out.println("getAllCustomersAsList");
        CustomerDao customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        List<Customer> expectedCustomers = new ArrayList<>();
        // Adjusted the list for the alphabetical order
        expectedCustomers.add(new Customer(4,"Fred","Boomsma",null,4));
        expectedCustomers.add(new Customer(3,"Jan","Jansen",null,3));
        expectedCustomers.add(new Customer(2,"Klaas","Klaassen",null,2));
        expectedCustomers.add(new Customer(1,"Piet","Pietersen",null,1));        
        expectedCustomers.add(new Customer(5,"Joost","Snel",null,5));
        List<Customer> allCustomers = customerDao.getAllCustomersAsList();
        assertEquals("All Customers should be as expected", expectedCustomers, allCustomers);
        System.out.println(allCustomers);
    }        
}
