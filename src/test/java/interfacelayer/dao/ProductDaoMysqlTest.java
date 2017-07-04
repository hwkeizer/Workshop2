/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Product;
import interfacelayer.DatabaseConnection;
import interfacelayer.DuplicateProductException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author hwkei
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class ProductDaoMysqlTest {
    
    private final String database = "applikaasie";
    private final int initialNumberOfProducts = 6; // Initial number of products
    
    public ProductDaoMysqlTest() {
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
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insertProduct method, of class ProductDaoMysql.
     */
    @Test
    public void testInsertProduct() throws DuplicateProductException {
        System.out.println("insertProduct");
        
        // Prepare a product to add to the database        
        String  testName = "Goudse inserted kaas";
        BigDecimal testPrice = new BigDecimal("13.55");
        Integer testStock = 125;
        Product testProduct = new Product(testName, testPrice, testStock);
        
        // Count the records before the insert
        int countBefore = getTableCount("product");
        
        // Add the prepared product to the database
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        productDao.insertProduct(testProduct);        
        
        // Count the records after the insert and compare with before
        assertEquals("Number of products should be increased by one.", countBefore + 1, getTableCount("product"));
        
        // Try to fetch the product from the database. If it exists and ID is not the same as already present in database, we have succesfully created a new product
        final String query = "SELECT * FROM `product` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfProducts + " AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testName);
            stat.setString(2,testPrice.toString());
            stat.setString(3,testStock.toString());
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
     * Test of insertProduct method, of class ProductDaoMysql.
     * Test if inserting existing product will throw Exception
     */
    @Test
    public void testInsertExistingProduct() {
        System.out.println("insertExistingProduct");
        
        // Prepare a product to add to the database        
        String  testName = "Leidse oude kaas";
        BigDecimal testPrice = new BigDecimal("14.65");
        Integer testStock = 89;
        Product testProduct = new Product(testName, testPrice, testStock);
        
        // Add the prepared product to the database
        try {
            ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
            productDao.insertProduct(testProduct);
            fail("Adding an existing product should have thrown a DuplicateProductException");
        } catch (DuplicateProductException ex) {
            // Assert expected exception
            assertTrue("Exception DuplicateProductException should be thrown", ex instanceof DuplicateProductException);
            assertEquals("Exception message should be as expected.", "Product with name = " + testProduct.getName() + " is already in the database", ex.getMessage());
        }       
    }
    
    /**
     * Test of findProductByName method, of class ProductDaoMysql.
     */
    @Test
    public void testFindProductByName() {
        System.out.println("findProductByName");
       
        // Define the product to be searched
        Product expectedProduct = new Product(2, "Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        String searchString = expectedProduct.getName();
        
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        Optional<Product> optionalProduct = productDao.findProductByName(searchString);
        
        // Assert we found the product and it is the product we expected
        assertTrue(optionalProduct.isPresent());
        assertEquals(expectedProduct, optionalProduct.get());
    }

    /**
     * Test of findProductById method, of class ProductDaoMysql.
     */
    @Test
    public void testFindProductById() {
        System.out.println("findProductById");
        
        // Define the product to be searched
        Product expectedProduct = new Product(2, "Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        int searchId = expectedProduct.getId();
        
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        Optional<Product> optionalProduct = productDao.findProductById(searchId);
        
        // Assert we found the product and it is the product we expected
        assertTrue(optionalProduct.isPresent());
        assertEquals(expectedProduct, optionalProduct.get());
    }

    /**
     * Test of deleteProduct method, of class ProductDaoMysql.
     */
    @Test
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        
        // Prepare the product to be deleted
        Integer testId = 3;
        String  testName = "Leidse oude kaas";
        BigDecimal testPrice = new BigDecimal("14.65");
        Integer testStock = 89;
        Product testProduct = new Product(testId, testName, testPrice, testStock);
        
        // Try to fetch the product from the database. It must exist or testing will make no sence
        final String query = "SELECT * FROM `product` WHERE `id`=? AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1, testId.toString());
            stat.setString(2, testName);
            stat.setString(3,testPrice.toString());
            stat.setString(4,testStock.toString());
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
        int countBefore = getTableCount("product");
        
        // Perform the deletion of the product
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();        
        productDao.deleteProduct(testProduct);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of products should be decreased by one.", countBefore - 1, getTableCount("product"));
        
        // Try to fetch the product from the database. If it does not exist we have succesfully deleted the product
        final String queryForDeleted = "SELECT * FROM `product` WHERE `id`=? AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setString(1, testId.toString());
            stat.setString(2, testName);
            stat.setString(3,testPrice.toString());
            stat.setString(4,testStock.toString());
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
    
    /**
     * Test of updateProduct method, of class ProductDaoMysql.
     */
    @Test
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        
        // Prepare a product to update in the database
        Integer testId = 4;
        String  testName = "Schimmelkaas";
        BigDecimal testPrice = new BigDecimal("11.74");
        Integer testStock = 256;
        Product testProduct = new Product(testId, testName, testPrice, testStock);
        
        // Set a new name, price and stock
        String newName = "Blauwe schimmelkaas";
        BigDecimal newPrice = new BigDecimal("12.78");
        Integer newStock = 119;
                
        testProduct.setName(newName);
        testProduct.setPrice(newPrice);
        testProduct.setStock(newStock);
        
        // Perform the update in the databse
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        productDao.updateProduct(testProduct);
                
        // Validate the update        
        final String query = "SELECT * FROM `product` WHERE `id`=? AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            // Try to find the product with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,testName);
            stat.setString(3,testPrice.toString());
            stat.setString(4,testStock.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the product with the new values in the database. This should succeed
            stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,newName);
            stat.setString(3,newPrice.toString());
            stat.setString(4,newStock.toString());
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

}
