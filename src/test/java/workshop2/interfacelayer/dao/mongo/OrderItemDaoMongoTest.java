/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import workshop2.domain.OrderItem;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderItemDao;

/**
 *
 * @author thoma
 */
public class OrderItemDaoMongoTest {
    
    public OrderItemDaoMongoTest() {
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
     * Test of insertOrderItem method, of class OrderItemDaoMongo.
     */
    @Test
    public void testInsertOrderItem() throws Exception {
    System.out.println("insertOrderItem");
        // Get the account Collection for easy test verification
        MongoCollection orderItemCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order_item");        

        // Prepare an order item to add to the database        
        Integer testOrderId = 3;
        Integer testProductId = 6;
        Integer testAmount = 12;
        BigDecimal testSubTotal = new BigDecimal("145.78");
        OrderItem testOrderItem = new OrderItem(testOrderId, testProductId, testAmount, testSubTotal);
        
        // Count the records before the insert and verify the account is not yet in the database       
        long countBefore = orderItemCollection.count();
        BasicDBObject query = new BasicDBObject("subtotal", testSubTotal.toString());
        assertFalse("OrderItem should not be in database before insertion", orderItemCollection.find(query).iterator().hasNext());
        
        
        // Add the prepared orderItem to the database
        OrderItemDao orderItemDAO = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemDAO.insertOrderItem(testOrderItem);        
        
        // Verify the records after the insertion and verify the customer is inserted
        assertEquals("Number of orderItems should be increased by one.", countBefore + 1, orderItemCollection.count());
        assertTrue("OrderItem should be in database after insertion", orderItemCollection.find(query).iterator().hasNext());
   
    }
    
    /**
     * Test of updateOrderItem method, of class OrderItemDaoMongo.
     */
    @Test
    public void testUpdateOrderItem() {
        System.out.println("updateOrderItem");
        // Prepare a orderItem to add to the database   
        
        // Get the orderItem Collection for easy test verification
        MongoCollection orderItemCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order_item");
        
        // Prepare an order item to update in the database
        Integer testId = 9;
        Integer testOrderId = 6;
        Integer testProductId = 1;
        Integer testAmount = 3;
        BigDecimal testSubTotal = new BigDecimal("35.31");
        OrderItem testOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        
                
        // Set new username, password and orderItem type
        BigDecimal newSubtotal = new BigDecimal("55.13");
        Integer newAmount = 12;
                
        testOrderItem.setSubTotal(newSubtotal);
        testOrderItem.setAmount(newAmount);
             
        // Count the number of orderItems before the update and verify the old orderItem is and the 
        // new orderItem is not in the database
        long countBefore = orderItemCollection.count();
        BasicDBObject queryOld = new BasicDBObject("subtotal", testSubTotal.toString());
        BasicDBObject queryNew = new BasicDBObject("subtotal", newSubtotal.toString());
        assertTrue("Old OrderItem should be in database before update", orderItemCollection.find(queryOld).iterator().hasNext());
        assertFalse("New OrderItem should not be in database before update", orderItemCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemDao.updateOrderItem(testOrderItem);
                
        // Validate the old orderItem is not and the new orderItem is in the database and
        // the number of orderItems is the same as before
        assertFalse("Old OrderItem should not be in database after update", orderItemCollection.find(queryOld).iterator().hasNext());
        assertTrue("New OrderItem should be in database after update", orderItemCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of orderItems should not have changed after update", countBefore, orderItemCollection.count());
    }
    
    /**
     * Test of deleteOrderItem method, of class OrderItemDaoMongo.
     */
    @Test
    public void testDeleteOrderItem() {
        
        // Prepare the orderItem to be deleted
        Integer testId = 7;
        Integer testOrderId = 4;
        Integer testProductId = 5;
        Integer testAmount = 2;
        BigDecimal testSubTotal = new BigDecimal("23.78");
        OrderItem testOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        
        // Get the orderItemion Collection for easy test verification
        MongoCollection orderItemCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order_item"); 
        
        // Count the records before the deletion and verify the orderItem is in the database       
        long countBefore = orderItemCollection.count();
        BasicDBObject query = new BasicDBObject("subtotal", testSubTotal.toString());
        assertTrue("OrderItem should be in database before deletion", orderItemCollection.find(query).iterator().hasNext());
        
        // Delete the prepared orderItem from the database with the DAO
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemDao.deleteOrderItem(testOrderItem);        
        
        // Verify the records after the insertion and verify the orderItem is deleted
        assertEquals("Number of orderItems should be increased by one.", countBefore - 1, orderItemCollection.count());
        assertFalse("OrderItem should not be in database after deletion", orderItemCollection.find(query).iterator().hasNext());
    }
    
    /**
     * Test of findOrderItemById method, of class OrderItemDaoMongo.
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
        
        // Assert we found the orderItem and it is the orderItem we expected
        assertTrue("Existing orderItem should be present", optionalOrderItem.isPresent());
        assertEquals("Existing orderItem should be the expected orderItem", expectedOrderItem, optionalOrderItem.get());
    }
    
    /**
     * Test of findOrderItemById method, of class OrderItemDaoMongo.
     */
    @Test
    public void testFindNonExistingOrderItemById() {
        System.out.println("findNonExistingOrderItemById");
        // Define the orderItem to be searched
        Integer testId = 100;
        Integer testOrderId = 8;
        Integer testProductId = 4;
        Integer testAmount = 23;
        BigDecimal testSubTotal = new BigDecimal("167.32");
        OrderItem expectedOrderItem = new OrderItem(testId, testOrderId, testProductId, testAmount, testSubTotal);
        int searchId = expectedOrderItem.getId();
        
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        Optional<OrderItem> optionalOrderItem = orderItemDao.findOrderItemById(searchId);
        
        // Assert we did not find the orderItem
        assertFalse("Non Existing orderItem should not be present", optionalOrderItem.isPresent());
    }
    
     /**
     * Test of getAllOrderItemsAsList method, of class OrderItemDaoMongo.
     */
    @Test
    public void testfindAllOrderItemsAsListByOrderId() {
        System.out.println("getAllOrderItemsAsList");
               
        //declare and get the orderItemlist to be tested
        List<OrderItem> orderItemList;
        OrderItemDao orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        orderItemList = orderItemDao.findAllOrderItemsAsListByOrderId(1);
        
        // Assert we found the orderItemList and it is the orderItemList we expected
        assertEquals("Number of items in the list should equal initial number of orderItems in database", 3, orderItemList.size());
        assertEquals("First orderItem in the list equals first entry in the database", "254.12" , orderItemList.get(0).getSubTotal().toString());
        assertEquals("First orderItem in the list equals first entry in the database", 2, orderItemList.get(2).getAmount());
        assertEquals("First orderItem in the list equals first entry in the database", 1, orderItemList.get(1).getProductId());
    }
}
