/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.ProductDao;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    
    private final int initialNumberOfProducts = 6; // Initial number of products
    
    public ProductDaoMysqlTest() {
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
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        productDao.insertProduct(testProduct);        
        
        // Count the records after the insert and compare with before
        assertEquals("Number of products should be increased by one.", countBefore + 1, getTableCount("product"));
        
        // Try to fetch the product from the database. If it exists and ID is not the same as already present in database, we have succesfully created a new product
        final String query = "SELECT * FROM `product` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfProducts + " AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
            ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
            productDao.insertProduct(testProduct);
            fail("Adding an existing product should have thrown a DuplicateProductException");
        } catch (DuplicateProductException ex) {
            // Assert expected exception
            assertTrue("Exception DuplicateProductException should be thrown", ex instanceof DuplicateProductException);
            assertEquals("Exception message should be as expected.", "Product with name = " + testProduct.getName() + " is already in the database", ex.getMessage());
        }       
    }
    
    /**
     * Test of findExistingProductByName method, of class ProductDaoMysql.
     */
    @Test
    public void testFindExistingProductByName() {
        System.out.println("findExistingProductByName");
       
        // Define the product to be searched
        Product expectedProduct = new Product(2, "Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        String searchString = expectedProduct.getName();
        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        Optional<Product> optionalProduct = productDao.findProductByName(searchString);
        
        // Assert we found the product and it is the product we expected
        assertTrue("Existing product should be present", optionalProduct.isPresent());
        assertEquals("Existing product should be the expected product", expectedProduct, optionalProduct.get());
    }
    
    /**
     * Test of findNonExistingProductByName method, of class ProductDaoMysql.
     */
    @Test
    public void testFindNonExistingProductByName() {
        System.out.println("findNonExistingProductByName");
       
        // Define the product to be searched
        Product expectedProduct = new Product(2, "verkeerde naam", new BigDecimal("14.70"), 239);
        String searchString = expectedProduct.getName();
        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        Optional<Product> optionalProduct = productDao.findProductByName(searchString);
        
        // Assert we did not find the product
        assertFalse("Non existing product should not be present", optionalProduct.isPresent());
    }
    
    /**
     * Test of findExistingProductById method, of class ProductDaoMysql.
     */
    @Test
    public void testFindExistingProductById() {
        System.out.println("findExistingProductById");
        
        // Define the product to be searched
        Product expectedProduct = new Product(2, "Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        int searchId = expectedProduct.getId();
        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        Optional<Product> optionalProduct = productDao.findProductById(searchId);
        
        // Assert we found the product and it is the product we expected
        assertTrue("Existing product should be present", optionalProduct.isPresent());
        assertEquals("Existing product should be the expected product",expectedProduct, optionalProduct.get());
    }    
    
    /**
     * Test of findNonExistingProductById method, of class ProductDaoMysql.
     */
    @Test
    public void testFindNonExistingProductById() {
        System.out.println("findNonExistingProductById");
        
        // Define the product to be searched
        Product expectedProduct = new Product(20, "Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        int searchId = expectedProduct.getId();
        
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        Optional<Product> optionalProduct = productDao.findProductById(searchId);
        
        // Assert we did not find the product
        assertFalse("Non Existing product should not be present", optionalProduct.isPresent());
    }
    
        /**
     * Test of getAllProductsAsList method, of class ProductDaoMysql.
     */
    @Test
    public void testGetAllProductsAsList() {
        System.out.println("getAllProductsAsList");
        
        //declare and get the productlist to be tested
        List<Product> productList = new ArrayList<>();
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        productList = productDao.getAllProductsAsList();
        

        
        // Assert we found the productList and it is the productList we expected
        assertEquals("Number of items in the list should equal initial number of products in database", initialNumberOfProducts, productList.size());
        assertEquals("First product in the list equals first entry in the database", "Boeren jonge kaas" , productList.get(0).getName());
        assertEquals("First product in the list equals first entry in the database", "14.70" , productList.get(2).getPrice().toString());
        assertEquals("First product in the list equals first entry in the database", 122 , productList.get(3).getStock());

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
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();        
        productDao.deleteProduct(testProduct);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of products should be decreased by one.", countBefore - 1, getTableCount("product"));
        
        // Try to fetch the product from the database. If it does not exist we have succesfully deleted the product
        final String queryForDeleted = "SELECT * FROM `product` WHERE `id`=? AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        productDao.updateProduct(testProduct);
                
        // Validate the update        
        final String query = "SELECT * FROM `product` WHERE `id`=? AND `name`=? AND `price`=? AND `stock`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
