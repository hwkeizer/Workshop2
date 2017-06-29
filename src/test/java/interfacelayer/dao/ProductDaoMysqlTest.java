/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import interfacelayer.dao.ProductDao;
import interfacelayer.dao.DaoFactory;
import domain.Product;
import interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hwkei
 */
public class ProductDaoMysqlTest {
    
    public ProductDaoMysqlTest() {
    }
    
    @Before
    public void setUp() {        
        // Clear the database before starting to test
        try (Connection connection = DatabaseConnection.getInstance().getConnection();) {
            Statement statement = connection.createStatement();        
            statement.executeUpdate("DELETE FROM `address`;");            
            statement.executeUpdate("DELETE FROM `order_item`;");            
            statement.executeUpdate("DELETE FROM `order`;");            
            statement.executeUpdate("DELETE FROM `product`;");            
            statement.executeUpdate("DELETE FROM `customer`;");            
            statement.executeUpdate("DELETE FROM `account`;");            
            statement.executeUpdate("DELETE FROM `account_type`;");            
            statement.executeUpdate("DELETE FROM `address_type`;");            
            statement.executeUpdate("DELETE FROM `order_status`;");
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
    public void testInsertProduct() {
        System.out.println("insertProduct");
        
        // Add a test product to the database
        DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        ProductDao productDAO = daoFactory.getProductDAO();        
        Product productInserted = new Product("Goudse inserted kaas", new BigDecimal("13.55"), 125);
        
        productDAO.insertProduct(productInserted);
        
        // Search the product by name to find it back in the database
        Product productFound = productDAO.findProductByName("Goudse inserted kaas");
        
        // Validate the inserted product 
        assertEquals(productInserted.getName(), productFound.getName());
        assertEquals(productInserted.getPrice(), productFound.getPrice());
        assertEquals(productInserted.getStock(), productFound.getStock());
        
        // Try to insert the same product again, this should notify the user with an error
        productDAO.insertProduct(productInserted);
    }

    /**
     * Test of updateProduct method, of class ProductDaoMysql.
     */
    @Test
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        
        // Add a test product to the database
        DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        ProductDao productDAO = daoFactory.getProductDAO();
        Product product = new Product("Goudse jonge kaas", new BigDecimal("15.55"), 125);
        productDAO.insertProduct(product);
        
        // Retrieve the product from the database
        Product updatedProduct = productDAO.findProductByName("Goudse jonge kaas");
        updatedProduct.setName("Goudse updated kaas");
        productDAO.updateProduct(updatedProduct);
                
        // Validate the update
        assertNull("After update old name should not be found anymore", 
                productDAO.findProductByName("Goudse jonge kaas"));
        assertNotNull("After update new name should be found", 
                productDAO.findProductByName("Goudse updated kaas"));
    }

    /**
     * Test of deleteProduct method, of class ProductDaoMysql.
     */
    @Test
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        
        // Add a test product to the database
        DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        ProductDao productDAO = daoFactory.getProductDAO();        
        Product product = new Product("Leidse belegen kaas", new BigDecimal("14.55"), 100);
        productDAO.insertProduct(product);
        
        // Retrieve the product id back from the database
        product = productDAO.findProductByName("Leidse belegen kaas");
        int productToDelete = product.getId();
        
        productDAO.deleteProduct(product);
        assertNull("Deleted product should not be found by name", 
                productDAO.findProductByName("Leidse belegen kaas"));
        assertNull("Deleted product should not be found by id", 
                productDAO.findProductById(productToDelete));
    }

    /**
     * Test of findProductById method, of class ProductDaoMysql.
     */
    @Test
    public void testFindProductById() {
        System.out.println("findProductById");
        
        // Add two test products to the database
        DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        ProductDao productDAO = daoFactory.getProductDAO();
        Product product1 = new Product("Goudse oude kaas", new BigDecimal("18.55"), 75);
        Product product2 = new Product("Boeren oude kaas", new BigDecimal("19.55"), 12);
        productDAO.insertProduct(product1);
        productDAO.insertProduct(product2);
        
        // Retrieve one of the test products by name to get the id
        Product product3 = productDAO.findProductByName("Goudse oude kaas");
        
        // Find the product by id and validate it is the correct one
        Product product4 = productDAO.findProductById(product3.getId());
        assertEquals("Id of found product should equal searched product id", 
                product3.getId(), product4.getId());
        assertEquals("Name of found product should equal searched product name", 
                product3.getName(), product4.getName());
        assertEquals("Price of found product should equal searched product price", 
                product3.getPrice(), product4.getPrice());
        assertEquals("Stock of found product should equal searched product stock", 
                product3.getStock(), product4.getStock());
    }

    /**
     * Test of findProductByName method, of class ProductDaoMysql.
     */
    @Test
    public void testFindProductByName() {
        System.out.println("findProductByName");
        
        // Add a test product to the database
        DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        ProductDao productDAO = daoFactory.getProductDAO();
        Product product1 = new Product("Goudse overjarige kaas", new BigDecimal("22.55"), 75);
        productDAO.insertProduct(product1);
        
        // Search the product on name (to retrieve the id) 
        Product product3 = productDAO.findProductByName("Goudse overjarige kaas");
        
        // Validate we found the same product that we inserted
        assertEquals("Name of found product should equal searched product name", 
                product3.getName(), product1.getName());
        assertEquals("Price of found product should equal searched product price", 
                product3.getPrice(), product1.getPrice());
        assertEquals("Stock of found product should equal searched product stock", 
                product3.getStock(), product1.getStock());
    }
    
}
