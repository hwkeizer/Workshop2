/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderItemDao;
import workshop2.domain.OrderItem;
import workshop2.interfacelayer.DatabaseConnection;
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
    
    private final int initialNumberOfOrderItems = 15; // Initial number of products
    
        @Before
    public void initializeDatabase() {        
        DatabaseConnection.getInstance().setDatabaseType("MYSQL");
        DatabaseTest.initializeDatabase();
        DatabaseTest.populateDatabase();
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
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemDao.insertOrderItem(testOrderItem);        
        
        // Count the records after the insert and compare with before
        assertEquals("Number of order items should be increased by one.", countBefore + 1, getTableCount("order_item"));
        
        // Try to fetch the order item from the database. If it exists and ID is not the same as already present in database, we have succesfully created a new order item
        final String query = "SELECT * FROM `order_item` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfOrderItems + " AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemDao.updateOrderItem(testOrderItem);
                
        // Validate the update        
        final String query = "SELECT * FROM `order_item` WHERE `id`=? AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();        
        orderItemDao.deleteOrderItem(testOrderItem);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of products should be decreased by one.", countBefore - 1, getTableCount("order_item"));
        
        // Try to fetch the product from the database. If it does not exist we have succesfully deleted the product
        final String queryForDeleted = "SELECT * FROM `order_item` WHERE `id`=? AND `order_id`=? AND `product_id`=? AND `amount`=? AND `subtotal`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
        
        // Define the orderItem to be searched
        Integer testId = 13;
        Integer testOrderId = 8;
        Integer testProductId = 4;
        Integer testAmount = 23;
        BigDecimal testSubTotal = new BigDecimal("167.32");
        OrderItem expectedOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        int searchId = expectedOrderItem.getId();
        
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
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
        
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        Optional<OrderItem> optionalOrderItem = orderItemDao.findOrderItemById(searchId);
        
        // Assert we did not find the OrderItem
        assertFalse("Non existing OrderItem should not be found", optionalOrderItem.isPresent());
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
                System.out.println("SQL Exception: 3" + ex.getMessage());
            }            
        } catch (SQLException ex) {
            System.out.println("SQL Exception: 4" + ex.getMessage());
        }
        return -1;
    }
}
