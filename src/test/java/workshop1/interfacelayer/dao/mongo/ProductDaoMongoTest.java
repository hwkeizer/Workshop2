/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop1.domain.Product;
import workshop1.interfacelayer.DatabaseConnection;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.DuplicateProductException;
import workshop1.interfacelayer.dao.ProductDao;

/**
 *
 * @author hwkei
 */
public class ProductDaoMongoTest {
    
    public ProductDaoMongoTest() {
    }
    
     @Before
    public void initializeDatabase() {        
        DatabaseTest.initializeMongoDatabase();
        DatabaseTest.populateMongoDatabase();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insertProduct method, of class ProductDaoMongo.
     */
    @Test
    public void testInsertProduct() throws Exception {
        System.out.println("insertProduct");
        // Get the production Collection for easy test verification
        MongoCollection productCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("product");        

         // Prepare a product to add to the database        
        String  testName = "Goudse inserted kaas";
        BigDecimal testPrice = new BigDecimal("13.55");
        Integer testStock = 125;
        Product testProduct = new Product(testName, testPrice, testStock);
        
        // Count the records before the insert and verify the product is not yet in the database       
        long countBefore = productCollection.count();
        BasicDBObject query = new BasicDBObject("name", testName);
        assertFalse("Product should not be in database before insertion", productCollection.find(query).iterator().hasNext());
        
        // Add the prepared product to the database with the DAO
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MONGO).createProductDao();
        productDao.insertProduct(testProduct);        
        
        // Verify the records after the insertion and verify the product is inserted
        assertEquals("Number of products should be increased by one.", countBefore + 1, productCollection.count());
        assertTrue("Product should be in databse after insertion", productCollection.find(query).iterator().hasNext());
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
            ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MONGO).createProductDao();
            productDao.insertProduct(testProduct);
            fail("Adding an existing product should have thrown a DuplicateProductException");
        } catch (DuplicateProductException ex) {
            // Assert expected exception
            assertTrue("Exception DuplicateProductException should be thrown", ex instanceof DuplicateProductException);
            assertEquals("Exception message should be as expected.", "Product with name = " + testProduct.getName() + " is already in the database", ex.getMessage());
        }       
    }

    /**
     * Test of updateProduct method, of class ProductDaoMongo.
     */
    @Test
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        // Get the production Collection for easy test verification
        MongoCollection productCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("product");
        
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
        
        // Count the number of products before the update and verify the old product is and the 
        // new product is not in the database
        long countBefore = productCollection.count();
        BasicDBObject queryOld = new BasicDBObject("name", testName);
        BasicDBObject queryNew = new BasicDBObject("name", newName);
        assertTrue("Old Product should be in database before update", productCollection.find(queryOld).iterator().hasNext());
        assertFalse("New Product should not be in database before update", productCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MONGO).createProductDao();
        productDao.updateProduct(testProduct);
                
        // Validate the old product is not and the new product is in the database and
        // the number of products is the same as before
        assertFalse("Old Product should not be in database after update", productCollection.find(queryOld).iterator().hasNext());
        assertTrue("New Product should be in database after update", productCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of products should not have changed after update", countBefore, productCollection.count());
    }

    /**
     * Test of deleteProduct method, of class ProductDaoMongo.
     */
    @Test
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        Product product = null;
        ProductDaoMongo instance = new ProductDaoMongo();
        instance.deleteProduct(product);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findProductById method, of class ProductDaoMongo.
     */
    @Test
    public void testFindProductById() {
        System.out.println("findProductById");
        int productId = 0;
        ProductDaoMongo instance = new ProductDaoMongo();
        Optional<Product> expResult = null;
        Optional<Product> result = instance.findProductById(productId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findProductByName method, of class ProductDaoMongo.
     */
    @Test
    public void testFindProductByName() {
        System.out.println("findProductByName");
        String name = "";
        ProductDaoMongo instance = new ProductDaoMongo();
        Optional<Product> expResult = null;
        Optional<Product> result = instance.findProductByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllProductsAsList method, of class ProductDaoMongo.
     */
    @Test
    public void testGetAllProductsAsList() {
        System.out.println("getAllProductsAsList");
        ProductDaoMongo instance = new ProductDaoMongo();
        ArrayList<Product> expResult = null;
        ArrayList<Product> result = instance.getAllProductsAsList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
