/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import workshop1.interfacelayer.dao.AddressDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.domain.Account;
import workshop1.domain.Address;
import workshop1.interfacelayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author thoma
 */
@Ignore("Temporary ignore to speed up testing of other DAO's")
public class AddresDaoMysqlTest {
    private static final Logger log = LoggerFactory.getLogger(AddresDaoMysqlTest.class);
    private final int initialNumberOfAddresses = 7; // Initial number of accounts
    
    public AddresDaoMysqlTest() {
    }
    
    @Before
    public void initializeDatabase() {        
        DatabaseTest.initializeDatabase();
        DatabaseTest.populateDatabase();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of insertAccount method, of class AccountDaoMysql.
     */
    @Test
    //@Ignore
    public void testInsertAddress() {
        System.out.println("insertAddress");
        
        //Prepare an address to add to the database
        String testStreetName = "dinges";
        Integer testNumber = 5;
        String testAddition = "b";
        String testPostalCode = "2546CH";
        String testCity = "Utrecht";
        Integer testCustomerId = 5;
        Integer testAddressTypeId = 2;
        Address testAddress = new Address(testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);

        // Count the records before the insert
        int countBefore = getTableCount("address");

        // Add a test address to the database
        //DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();        
        addressDao.insertAddress(testAddress);
        
        // Count the records after the insert and compare with before
        assertEquals("Number of addresss should be increased by one.", countBefore + 1, getTableCount("address"));

        // Try to fetch the address from the database. If it exists and ID is not the same as allready present in database, we have succesfully created a new address
        final String query = "SELECT * FROM `address` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfAddresses + " AND `street_name`=? AND `number`=? AND `addition`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testStreetName);
            stat.setInt(2,testNumber);
            stat.setString(3,testAddition);
            stat.setString(4,testPostalCode);
            stat.setString(5,testCity);
            stat.setInt(6,testCustomerId);
            stat.setInt(7,testAddressTypeId);
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
     * Test of insertAddress method, of class AddressDaoMysql.
     * Test if inserting existing address will throw Exception
     */
//    @Test
//    public void testInsertExistingAddress() {
//        System.out.println("insertExistingAddress");
//        
//        // Prepare a address to add to the database        
//        Integer id = 3;
//        String testUsername = "jan";
//        String testPassword = "welkom";
//        Integer testAddressTypeId = 3;
//        Address testAddress = new Address(id, testUsername, testPassword, testAddressTypeId);
//        
//        // Add the prepared address to the database
//        try {
//            AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
//            addressDao.insertAddress(testAddress);
//            fail("Adding an existing address should have thrown a DuplicateAddressException");
//        } catch (DuplicateAddressException ex) {
//            // Assert expected exception
//            assertTrue("Exception DuplicateAddressException should be thrown", ex instanceof DuplicateAddressException);
//            assertEquals("Exception message should be as expected.", "Address with name = " + testAddress.getUsername() + " is already in the database", ex.getMessage());
//        }       
//    }

    /**
     * Test of findExistingAddressById method, of class AddressDaoMysql.
     */
    @Test
    public void testFindExistingAddressById() {
        System.out.println("findExistingAddressById");
        
        // Define the address to be searched
        Address expectedAddress = new Address(3, "Torenstraat", 82, null, "7620CX", "Best", 2, 2);
        int searchId = expectedAddress.getId();
        
        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
        Optional<Address> optionalAddress = addressDao.findAddressById(searchId);
        
        // Assert we found the address and it is the address we expected
        assertTrue("Existing Address should be present", optionalAddress.isPresent());
        assertEquals("Existing Address should be as expected", expectedAddress, optionalAddress.get());
    }
    
    /**
     * Test of findNonExistingAddressById method, of class AddressDaoMysql.
     */
    @Test
    public void testFindNonExistingAddressById() {
        System.out.println("findNonExistingAddressById");
        
        // Define the address to be searched
        Address expectedAddress = new Address(30, "Torenstraat", 82, null, "7620CX", "Best", 2, 2);
        int searchId = expectedAddress.getId();
        
        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
        Optional<Address> optionalAddress = addressDao.findAddressById(searchId);
        
        // Assert we did not find the address
        assertFalse("Non existing Address should not be present", optionalAddress.isPresent());
    }
    
//    /**
//     * Test of findAddressByCustomerId method, of class AddressDaoMysql.
//     */
//    @Test
//    public void testFindAddressByCustomerId() {
//        System.out.println("findAddressById");
//        
//        // Define the address to be searched
//        Address expectedAddress = new Address(1, "Postweg", 201, "h", "3781JK", "Aalst", 1, 1);
//        int searchCustomerId = expectedAddress.getCustomerId();
//        
//        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
//        Optional<Address> optionalAddress = addressDao.findAddressById(searchCustomerId);
//        
//        // Assert we found the address and it is the address we expected
//        assertTrue(optionalAddress.isPresent());
//        assertEquals(expectedAddress, optionalAddress.get());
//    }
    
    /**
     * Test of deleteAddress method, of class AddressDaoMysql.
     */
    @Test
    public void testDeleteAddress() {
        System.out.println("deleteAddress");
        
        // Prepare the address to be deleted   
        Integer testId = 6;
        String testStreetName = "Plein";
        Integer testNumber = 45;
        String testAddition = null;
        String testPostalCode = "2522BH";
        String testCity = "Oss";
        Integer testCustomerId = 4;
        Integer testAddressTypeId = 1;
        Address testAddress = new Address(testId, testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);

        
        // Try to fetch the address from the database. It must exist or testing will make no sence
        final String query;
        if(testAddition == null) {
            query = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition` is null";
        }
        else {
            query = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition`=?";
        }
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            
            stat.setInt(1,testId);
            stat.setString(2,testStreetName);
            stat.setInt(3,testNumber);
            stat.setString(4,testPostalCode);
            stat.setString(5,testCity);
            stat.setInt(6,testCustomerId);
            stat.setInt(7,testAddressTypeId);
            if(testAddition != null)    
                stat.setString(8,testAddition);
            
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue("Address should exist before testing delete", resultSet.next());                 
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals("Address should only exist once before a valid delete test can be done", 1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
        
        // Count the records bedore the deletion
        int countBefore = getTableCount("address");
        log.debug("count before is " + countBefore);
        
        // Perform the deletion of the address
        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();        
        addressDao.deleteAddress(testAddress);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of addresss should be decreased by one.", countBefore - 1, getTableCount("address"));
        
        // Try to fetch the address from the database. If it does not exist we have succesfully deleted the address
        final String queryForDeleted;
        if(testAddition == null) {
            queryForDeleted = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition` is null";
        }
        else {
            queryForDeleted = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition`=?";
        }
        
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setInt(1,testId);
            stat.setString(2,testStreetName);
            stat.setInt(3,testNumber);
            stat.setString(4,testPostalCode);
            stat.setString(5,testCity);
            stat.setInt(6,testCustomerId);
            stat.setInt(7,testAddressTypeId);
            if(testAddition != null){    
                stat.setString(8,testAddition);
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
     * Test of updateAddress method, of class AddressDaoMysql.
     */
    @Test
    public void testUpdateAddress() {
        System.out.println("updateAddress");
        
        //Prepare an address to be updated     
        Integer testId = 6;
        String testStreetName = "Plein";
        Integer testNumber = 45;
        String testAddition = null;
        String testPostalCode = "2522BH";
        String testCity = "Oss";
        Integer testCustomerId = 4;
        Integer testAddressTypeId = 1;
        Address testAddress = new Address(testId, testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);

        // Set new username, password and address type
        int newNumber = 6;
        String newAddition = "bis";
        String newStreetName = "Naamloos";
        String newPostalCode = "1234AB";
        String newCity = "Verweggistan";
        Integer newAddressTypeId = 1;
        
        testAddress.setNumber(newNumber);
        testAddress.setAddition(newAddition);
        testAddress.setStreetName(newStreetName);
        testAddress.setPostalCode(newPostalCode);
        testAddress.setCity(newCity);
        testAddress.setAddressTypeId(newAddressTypeId);
        
        // Perform the update in the databse
        AddressDao addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
        addressDao.updateAddress(testAddress);
        
        //Validate the update        
        final String beforeAddressUpdateQuery;
        if(testAddition == null) {
            beforeAddressUpdateQuery = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition` is null";
        }
        else {
            beforeAddressUpdateQuery = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition`=?";
        }
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            // Try to find the address with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(beforeAddressUpdateQuery);
            stat.setInt(1,testId);
            stat.setString(2,testStreetName);
            stat.setInt(3,testNumber);
            stat.setString(4,testPostalCode);
            stat.setString(5,testCity);
            stat.setInt(6,testCustomerId);
            stat.setInt(7,testAddressTypeId);
            if(testAddition != null)    
                stat.setString(8,testAddition);
            
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
                log.debug("Did the check for old address work?");
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the address with the new values in the database. This should succeed
            final String afterAddressUpdateQuery;
            if(newAddition == null) {
                afterAddressUpdateQuery = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition` is null";
            }
            else {
                afterAddressUpdateQuery = "SELECT * FROM `address` WHERE `id`=? AND `street_name`=? AND `number`=? AND `postal_code`=? AND `city`=? AND `customer_id`=? AND `address_type_id`=? AND `addition`=?";
            }
            stat = connection.prepareStatement(afterAddressUpdateQuery);
            stat.setInt(1,testId); //Not changed, need to keep the same ID, otherwise it's not an update
            stat.setString(2,newStreetName);
            stat.setInt(3,newNumber);
            stat.setString(4,newPostalCode);
            stat.setString(5,newCity);
            stat.setInt(6,testCustomerId); //Not changed, irrelevant when handling one particular customer
            stat.setInt(7,newAddressTypeId);
            if(newAddition != null)    
                stat.setString(8,newAddition);
            
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
     * Helper function to get the number of records from a table
     */
    int getTableCount(String table) {

        final String countQuery = "SELECT COUNT(*) FROM `" + table + "`";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
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
}
