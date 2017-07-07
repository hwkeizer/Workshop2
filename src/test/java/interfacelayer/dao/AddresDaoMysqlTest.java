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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author thoma
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class AddresDaoMysqlTest {
    private static final Logger log = LoggerFactory.getLogger(AddresDaoMysqlTest.class);
    private final String database = "applikaasie";
    private final int initialNumberOfAddresses = 7; // Initial number of accounts
    
    public AddresDaoMysqlTest() {
    }
    
    @Before
    public void initializeDatabase() {        
            // Prepare the SQL statements to drop the database and recreate it
        String dropDatabase = "DROP DATABASE IF EXISTS " + database;
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + database;
        String create_account_type = "CREATE TABLE IF NOT EXISTS `"+database+"`.`account_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String create_account = "CREATE TABLE IF NOT EXISTS `"+database+"`.`account` (`id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(25) NOT NULL, `password` VARCHAR(180) NOT NULL, `account_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_account_account_type1_idx` (`account_type_id` ASC), CONSTRAINT `fk_account_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `"+database+"`.`account_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String create_customer = "CREATE TABLE IF NOT EXISTS `"+database+"`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `first_name` VARCHAR(50) NOT NULL, `last_name` VARCHAR(50) NOT NULL, `ln_prefix` VARCHAR(15) NULL, `account_id` INT NULL, PRIMARY KEY (`id`), INDEX `fk_klant_account1_idx` (`account_id` ASC), CONSTRAINT `fk_klant_account1` FOREIGN KEY (`account_id`) REFERENCES `"+database+"`.`account` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String address_type = "CREATE TABLE IF NOT EXISTS `"+database+"`.`address_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String address = "CREATE TABLE IF NOT EXISTS `"+database+"`.`address` (`id` INT NOT NULL AUTO_INCREMENT, `street_name` VARCHAR(50) NOT NULL, `number` INT NOT NULL, `addition` VARCHAR(5) NULL, `postal_code` VARCHAR(6) NOT NULL, `city` VARCHAR(45) NOT NULL, `customer_id` INT NOT NULL, `address_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_adres_klant_idx` (`customer_id` ASC), INDEX `fk_adres_adres_type1_idx` (`address_type_id` ASC), CONSTRAINT `fk_adres_klant` FOREIGN KEY (`customer_id`) REFERENCES `"+database+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_adres_adres_type1` FOREIGN KEY (`address_type_id`) REFERENCES `"+database+"`.`address_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String order_status = "CREATE TABLE IF NOT EXISTS `"+database+"`.`order_status` (`id` INT NOT NULL AUTO_INCREMENT, `status` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String order = "CREATE TABLE IF NOT EXISTS `"+database+"`.`order` (`id` INT NOT NULL AUTO_INCREMENT, `total_price` DECIMAL(6,2) NOT NULL, `customer_id` INT NOT NULL, `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `order_status_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_bestelling_klant1_idx` (`customer_id` ASC),  INDEX `fk_bestelling_bestelling_status1_idx` (`order_status_id` ASC), CONSTRAINT `fk_bestelling_klant1` FOREIGN KEY (`customer_id`) REFERENCES `"+database+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestelling_bestelling_status1` FOREIGN KEY (`order_status_id`) REFERENCES `"+database+"`.`order_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String product = "CREATE TABLE IF NOT EXISTS `"+database+"`.`product` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, `price` DECIMAL(6,2) NOT NULL, `stock` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `name_UNIQUE` (`name` ASC)) ENGINE = InnoDB";
        String order_item = "CREATE TABLE IF NOT EXISTS `"+database+"`.`order_item` (`id` INT NOT NULL, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+database+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+database+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String trigger = "CREATE DEFINER = CURRENT_USER TRIGGER `"+database+"`.`customer_BEFORE_DELETE` BEFORE DELETE ON `customer` FOR EACH ROW DELETE FROM account WHERE id = OLD.account_id;";
        
        // Prepare the SQL statements to insert the test data into the database
        // Data contains 6 initial products
        String insert_account_type = "INSERT INTO `"+database+"`.`account_type`(`id`,`type`) VALUES (1,\"admin\"),(2,\"medewerker\"),(3,\"klant\")";
        String insert_address_type = "INSERT INTO `"+database+"`.`address_type`(`id`,`type`) VALUES (1,\"postadres\"),(2,\"factuuradres\"),(3,\"bezorgadres\")";
        String insert_order_status = "INSERT INTO `"+database+"`.`order_status`(`id`,`status`) VALUES (1,\"nieuw\"),(2,\"in behandeling\"),(3,\"afgehandeld\")";
        String insert_account = "INSERT INTO `"+database+"`.`account`(`id`,`username`,`password`,`account_type_id`) VALUES (1,\"piet\",\"welkom\",1),(2,\"klaas\",\"welkom\",2),(3,\"jan\",\"welkom\",3),(4,\"fred\",\"geheim\",3),(5,\"joost\",\"welkom\",3)";
        String insert_customer = "INSERT INTO `"+database+"`.`customer`(`id`,`first_name`,`last_name`,`ln_prefix`,`account_id`) VALUES (1,\"Piet\",\"Pietersen\",null,1), (2,\"Klaas\",\"Klaassen\",null,2),(3,\"Jan\",\"Jansen\",null,3),(4,\"Fred\",\"Boomsma\",null,4),(5,\"Joost\",\"Snel\",null,5)";
        String insert_address = "INSERT INTO `"+database+"`.`address`(`id`,`street_name`,`number`,`addition`,`postal_code`,`city`,`customer_id`,`address_type_id`) VALUES (1,\"Postweg\",201,\"h\",\"3781JK\",\"Aalst\",1,1),(2,\"Snelweg\",56,null,\"3922JL\",\"Ee\",2,1),(3,\"Torenstraat\",82,null,\"7620CX\",\"Best\",2,2),(4,\"Valkstraat\",9,\"e\",\"2424DF\",\"Goorle\",2,3),(5,\"Dorpsstraat\",5,null,\"9090NM\",\"Best\",3,1),(6,\"Plein\",45,null,\"2522BH\",\"Oss\",4,1),(7,\"Maduralaan\",23,null,\"8967HJ\",\"Apeldoorn\",5,1)";
        String insert_order = "INSERT INTO `"+database+"`.`order`(`id`,`total_price`,`customer_id`,`date`,`order_status_id`) VALUES (1,230.78,1,\"2016-01-01 01:01:01\",3),(2,62.97,1,\"2016-05-02 01:01:01\",3),(3,144.12,1,\"2017-06-02 01:01:01\",2),(4,78.23,2,\"2017-04-08 01:01:01\",3),(5,6.45,3,\"2017-06-28 01:01:01\",1),(6,324.65,3,\"2017-06-07 01:01:01\",3),(7,46.08,3,\"2017-06-07 01:01:01\",2),(8,99.56,4,\"2017-06-17 01:01:01\",1),(9,23.23,5,\"2017-05-13 01:01:01\",3)";
        String insert_product = "INSERT INTO `"+database+"`.`product`(`id`,`name`,`price`,`stock`) VALUES (1,\"Goudse belegen kaas\",12.90,134),(2,\"Goudse extra belegen kaas\",14.70,239),(3,\"Leidse oude kaas\",14.65,89),(4,\"Schimmelkaas\",11.74,256),(5,\"Leidse jonge kaas\",11.24,122),(6,\"Boeren jonge kaas\",12.57,85)";
        String insert_order_item = "INSERT INTO `"+database+"`.`order_item`(`id`,`order_id`,`product_id`,`amount`,`subtotal`) VALUES (1,1,6,23,254.12),(2,1,1,26,345.20),(3,1,2,2,24.14),(4,2,1,25,289.89),(5,3,4,2,34.89),(6,4,2,13,156.76),(7,4,5,2,23.78),(8,5,2,2,21.34),(9,6,1,3,35.31),(10,6,3,1,11.23),(11,7,6,1,14.23),(12,7,2,3,31.87),(13,8,4,23,167.32),(14,9,1,1,11.34),(15,9,2,2,22.41)"; 
        
        // Execute the SQL statements to drop the database and recreate it
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            Statement stat = connection.createStatement();
            stat.executeUpdate(dropDatabase);
            stat.executeUpdate(createDatabase); // Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement. ExecuteQuery kan niet gebruikt worden voor DDL statements
            stat.executeUpdate(create_account_type);
            stat.executeUpdate(create_account);
            stat.executeUpdate(create_customer);
            stat.executeUpdate(address_type);
            stat.executeUpdate(address);            
            stat.executeUpdate(order_status);
            stat.executeUpdate(order);
            stat.executeUpdate(product);
            stat.executeUpdate(order_item);            
            stat.executeUpdate(trigger);
            
            // Execute the SQL statements to insert the test data into the database
            stat.executeUpdate(insert_account_type);
            stat.executeUpdate(insert_address_type);
            stat.executeUpdate(insert_order_status);
            stat.executeUpdate(insert_account);
            stat.executeUpdate(insert_customer);
            stat.executeUpdate(insert_address);
            stat.executeUpdate(insert_order);
            stat.executeUpdate(insert_product);
            stat.executeUpdate(insert_order_item);            
        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of insertAccount method, of class AccountDaoMysql.
     */
    @Test
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
