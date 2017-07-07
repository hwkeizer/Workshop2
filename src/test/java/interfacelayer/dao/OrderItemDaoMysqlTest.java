/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.OrderItemDao;
import workshop1.domain.OrderItem;
import workshop1.interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author hwkei
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class OrderItemDaoMysqlTest {
    
    private final String database = "applikaasie";
    private final int initialNumberOfOrderItems = 15; // Initial number of products
    
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
        String order_item = "CREATE TABLE IF NOT EXISTS `"+database+"`.`order_item` (`id` INT NOT NULL  AUTO_INCREMENT, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+database+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+database+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
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
            stat.executeUpdate(createDatabase);
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
    
    /**
     * Test of insertOrderItem method, of class OrderItemDaoMysql.
     */
    @Test
    public void testInsertOrderItem(){
        System.out.println("insertOrderItem");
        
        // Prepare an order item to add to the database        
        Integer testOrderId = 3;
        Integer testProductId = 1;
        Integer testAmount = 12;
        BigDecimal testSubTotal = new BigDecimal("145.78");
        OrderItem testOrderItem = new OrderItem(testOrderId, testProductId, testAmount, testSubTotal);
        
        // Count the records before the insert
        int countBefore = getTableCount("order_item");
        
        // Add the prepared product to the database
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();
        orderItemDao.insertOrderItem(testOrderItem);        
        
        // Count the records after the insert and compare with before
        assertEquals("Number of order items should be increased by one.", countBefore + 1, getTableCount("order_item"));
        
        // Try to fetch the order item from the database. If it exists and ID is not the same as already present in database, we have succesfully created a new order item
        final String query = "SELECT * FROM `order_item` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfOrderItems + " AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);            
            stat.setString(1,testOrderId.toString());
            stat.setString(2,testProductId.toString());
            stat.setString(3,testAmount.toString());
            stat.setString(4,testSubTotal.toString());
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
     * Test of updateOrderItem method, of class OrderItemDaoMysql.
     */
    @Test
    public void testUpdateOrderItem() {
        System.out.println("updateOrderItem");
        
        // Prepare an order item to update in the database
        Integer testId = 9;
        Integer testOrderId = 6;
        Integer testProductId = 1;
        Integer testAmount = 3;
        BigDecimal testSubTotal = new BigDecimal("35.31");
        OrderItem testOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        
        // Set a new name, price and stock
        Integer newOrderId = 5;
        Integer newProductId = 5;
        Integer newAmount = 5;
        BigDecimal newSubTotal = new BigDecimal("55.55");              
        testOrderItem.setOrderId(newOrderId);
        testOrderItem.setProductId(newProductId);
        testOrderItem.setAmount(newAmount);
        testOrderItem.setSubTotal(newSubTotal);
        
        // Perform the update in the databse
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();
        orderItemDao.updateOrderItem(testOrderItem);
                
        // Validate the update        
        final String query = "SELECT * FROM `order_item` WHERE `id`=? AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            // Try to find the order item with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,testOrderId.toString());
            stat.setString(3,testProductId.toString());
            stat.setString(4,testAmount.toString());
            stat.setString(5,testSubTotal.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the order item with the new values in the database. This should succeed
            stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,newOrderId.toString());
            stat.setString(3,newProductId.toString());
            stat.setString(4,newAmount.toString());
            stat.setString(5,newSubTotal.toString());
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
     * Test of deleteOrderItem method, of class OrderItemDaoMysql.
     */
    @Test
    public void testDeleteOrderItem() {
        System.out.println("deleteOrderItem");
        
        // Prepare an order item to be deleted from the database
        Integer testId = 2;
        Integer testOrderId = 1;
        Integer testProductId = 1;
        Integer testAmount = 26;
        BigDecimal testSubTotal = new BigDecimal("345.20");
        OrderItem testOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        
        // Try to fetch the product from the database. It must exist or testing will make no sence
        final String query = "SELECT * FROM `order_item` WHERE `id`=? AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, testId);
            stat.setInt(2, testOrderId);
            stat.setInt(3,testProductId);
            stat.setInt(4,testAmount);
            stat.setString(5, testSubTotal.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue("Product should exist before testing delete", resultSet.next());                 
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals("Product should only exist once before a valid delete test can be done", 1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
        
        // Count the records bedore the deletion
        int countBefore = getTableCount("order_item");
        
        // Perform the deletion of the product
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();        
        orderItemDao.deleteOrderItem(testOrderItem);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of products should be decreased by one.", countBefore - 1, getTableCount("order_item"));
        
        // Try to fetch the product from the database. If it does not exist we have succesfully deleted the product
        final String queryForDeleted = "SELECT * FROM `order_item` WHERE `id`=? AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setInt(1, testId);
            stat.setInt(2, testOrderId);
            stat.setInt(3,testProductId);
            stat.setInt(4,testAmount);
            stat.setString(5, testSubTotal.toString());
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
     * Test of findExistingOrderItemById method, of class OrderItemDaoMysql.
     */
    @Test
    public void testFindExistingOrderItemById() {
        System.out.println("findExistingOrderItemById");
        
        // Prepare an order item to be deleted from the database
        Integer testId = 13;
        Integer testOrderId = 8;
        Integer testProductId = 4;
        Integer testAmount = 23;
        BigDecimal testSubTotal = new BigDecimal("167.32");
        OrderItem expectedOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        int searchId = expectedOrderItem.getId();
        
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();
        Optional<OrderItem> optionalOrderItem = orderItemDao.findOrderItemById(searchId);
        
        // Assert we found the product and it is the product we expected
        assertTrue("Existing OrderItem should be found", optionalOrderItem.isPresent());
        assertEquals("Existing OrderItem should be the expected OrderItem", expectedOrderItem, optionalOrderItem.get());
    }
    
    /**
     * Test of findExistingOrderItemById method, of class OrderItemDaoMysql.
     */
    @Test
    public void testFindNonExistingOrderItemById() {
        System.out.println("findNonExistingOrderItemById");
        
        // Prepare an order item to be deleted from the database
        Integer testId = 130;
        Integer testOrderId = 8;
        Integer testProductId = 4;
        Integer testAmount = 23;
        BigDecimal testSubTotal = new BigDecimal("167.32");
        OrderItem expectedOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        int searchId = expectedOrderItem.getId();
        
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createOrderItemDao();
        Optional<OrderItem> optionalOrderItem = orderItemDao.findOrderItemById(searchId);
        
        // Assert we did not find the OrderItem
        assertFalse("Non existing OrderItem should not be found", optionalOrderItem.isPresent());
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
                System.out.println("SQL Exception: 3" + ex.getMessage());
            }            
        } catch (SQLException ex) {
            System.out.println("SQL Exception: 4" + ex.getMessage());
        }
        return -1;
    }
}
