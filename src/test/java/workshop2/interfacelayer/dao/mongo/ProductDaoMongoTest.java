/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.interfacelayer.dao.ProductDao;

/**
 *
 * @author hwkei
 */
public class ProductDaoMongoTest {
    
    public ProductDaoMongoTest() {
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
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
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
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
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
        // Prepare the product to be deleted
        Integer testId = 3;
        String  testName = "Leidse oude kaas";
        BigDecimal testPrice = new BigDecimal("14.65");
        Integer testStock = 89;
        Product testProduct = new Product(testId, testName, testPrice, testStock);
        
        // Get the production Collection for easy test verification
        MongoCollection productCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("product"); 
        
        // Count the records before the deletion and verify the product is in the database       
        long countBefore = productCollection.count();
        BasicDBObject query = new BasicDBObject("name", testName);
        assertTrue("Product should be in database before deletion", productCollection.find(query).iterator().hasNext());
        
        // Delete the prepared product from the database with the DAO
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        productDao.deleteProduct(testProduct);        
        
        // Verify the records after the insertion and verify the product is deleted
        assertEquals("Number of products should be increased by one.", countBefore - 1, productCollection.count());
        assertFalse("Product should not be in database after deletion", productCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of findProductById method, of class ProductDaoMongo.
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
        assertEquals("Existing product should be the expected product", expectedProduct, optionalProduct.get());
    }
    
    /**
     * Test of findProductById method, of class ProductDaoMongo.
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
     * Test of findProductByName method, of class ProductDaoMongo.
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
     * Test of findProductByName method, of class ProductDaoMongo.
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
     * Test of getAllProductsAsList method, of class ProductDaoMongo.
     */
    @Test
    public void testGetAllProductsAsList() {
        System.out.println("getAllProductsAsList");
        
        // Get the production Collection for easy test verification
        MongoCollection productCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("product");
        long count = productCollection.count();
        
        //declare and get the productlist to be tested
        List<Product> productList;
        ProductDao productDao = DaoFactory.getDaoFactory().createProductDao();
        productList = productDao.getAllProductsAsList();
        
        // Assert we found the productList and it is the productList we expected
        assertEquals("Number of items in the list should equal initial number of products in database", count, productList.size());
        assertEquals("First product in the list equals first entry in the database", "Goudse belegen kaas" , productList.get(0).getName());
        assertEquals("First product in the list equals first entry in the database", "14.65" , productList.get(2).getPrice().toString());
        assertEquals("First product in the list equals first entry in the database", 256 , productList.get(3).getStock());
    }
    
}
