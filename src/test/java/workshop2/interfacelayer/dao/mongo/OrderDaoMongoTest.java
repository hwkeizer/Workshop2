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
import workshop2.domain.Order;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderDao;

/**
 *
 * @author thoma
 */
public class OrderDaoMongoTest {
    
    public OrderDaoMongoTest() {
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
     * Test of insertOrder method, of class OrderDaoMongo.
     */
    @Test
    public void testInsertOrder() throws Exception {
    System.out.println("insertOrder");
        // Get the account Collection for easy test verification
        MongoCollection orderCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order");        

    
    
        // Prepare an order to add to the database        
        BigDecimal testTotalPrice = new BigDecimal("13.55");
        Integer testCustomerId = 3;
        Integer year = 2016;
        Integer month = 8;
        Integer day = 22;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 2;
        Order testOrder = new Order(testTotalPrice, testCustomerId, testDate, testOrderStatusId); 
        
        // Count the records before the insert and verify the account is not yet in the database       
        long countBefore = orderCollection.count();
        BasicDBObject query = new BasicDBObject("total_price", testTotalPrice.toString());
        assertFalse("Order should not be in database before insertion", orderCollection.find(query).iterator().hasNext());
        
        
        // Add the prepared order to the database
        OrderDao orderDAO = DaoFactory.getDaoFactory().createOrderDao();
        orderDAO.insertOrder(testOrder);        
        
        // Verify the records after the insertion and verify the customer is inserted
        assertEquals("Number of orders should be increased by one.", countBefore + 1, orderCollection.count());
        assertTrue("Order should be in database after insertion", orderCollection.find(query).iterator().hasNext());
   
    }
    
    /**
     * Test of updateOrder method, of class OrderDaoMongo.
     */
    @Test
    public void testUpdateOrder() {
        System.out.println("updateOrder");
        // Prepare a order to add to the database   
        
        // Get the order Collection for easy test verification
        MongoCollection orderCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order");
        
        Integer testId = 4;
        BigDecimal testTotalPrice = new BigDecimal("78.23");
        Integer testCustomerId = 2;
        Integer year = 2017;
        Integer month = 4;
        Integer day = 8;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 3;
        Order testOrder = new Order(testId, testTotalPrice, testCustomerId, testDate, testOrderStatusId);
        
                
        // Set new username, password and order type
        BigDecimal newTotalPrice = new BigDecimal("55.13");
        Integer newOrderStatusId = 3;
                
        testOrder.setTotalPrice(newTotalPrice);
        testOrder.setOrderStatusId(newOrderStatusId);
             
        // Count the number of orders before the update and verify the old order is and the 
        // new order is not in the database
        long countBefore = orderCollection.count();
        BasicDBObject queryOld = new BasicDBObject("total_price", testTotalPrice.toString());
        BasicDBObject queryNew = new BasicDBObject("total_price", newTotalPrice.toString());
        assertTrue("Old Order should be in database before update", orderCollection.find(queryOld).iterator().hasNext());
        assertFalse("New Order should not be in database before update", orderCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderDao.updateOrder(testOrder);
                
        // Validate the old order is not and the new order is in the database and
        // the number of orders is the same as before
        assertFalse("Old Order should not be in database after update", orderCollection.find(queryOld).iterator().hasNext());
        assertTrue("New Order should be in database after update", orderCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of orders should not have changed after update", countBefore, orderCollection.count());
    }
    
    /**
     * Test of deleteOrder method, of class OrderDaoMongo.
     */
    @Test
    public void testDeleteOrder() {
        // Prepare the order to be deleted
        Integer testId = 4;
        BigDecimal testTotalPrice = new BigDecimal("78.23");
        Integer testCustomerId = 2;
        Integer year = 2017;
        Integer month = 4;
        Integer day = 8;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 3;
        Order testOrder = new Order(testId, testTotalPrice, testCustomerId, testDate, testOrderStatusId);
        
        // Get the orderion Collection for easy test verification
        MongoCollection orderCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order"); 
        
        // Count the records before the deletion and verify the order is in the database       
        long countBefore = orderCollection.count();
        BasicDBObject query = new BasicDBObject("total_price", testTotalPrice.toString());
        assertTrue("Order should be in database before deletion", orderCollection.find(query).iterator().hasNext());
        
        // Delete the prepared order from the database with the DAO
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderDao.deleteOrder(testOrder);        
        
        // Verify the records after the insertion and verify the order is deleted
        assertEquals("Number of orders should be increased by one.", countBefore - 1, orderCollection.count());
        assertFalse("Order should not be in database after deletion", orderCollection.find(query).iterator().hasNext());
    }
    
    /**
     * Test of findOrderById method, of class OrderDaoMongo.
     */
    @Test
    public void testFindExistingOrderById() {
        System.out.println("findExistingOrderById");
        // Define the order to be searched
        LocalDateTime date = LocalDateTime.parse("2017-06-02T01:01:01");
        Order expectedOrder = new Order(3,new BigDecimal("144.12"), 1, date, 2);
        int searchId = expectedOrder.getId();
        
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        Optional<Order> optionalOrder = orderDao.findOrderById(searchId);
        
        // Assert we found the order and it is the order we expected
        assertTrue("Existing order should be present", optionalOrder.isPresent());
        assertEquals("Existing order should be the expected order", expectedOrder, optionalOrder.get());
    }
    
    /**
     * Test of findOrderById method, of class OrderDaoMongo.
     */
    @Test
    public void testFindNonExistingOrderById() {
        System.out.println("findNonExistingOrderById");
        // Define the order to be searched
        LocalDateTime date = LocalDateTime.parse("2017-06-02T01:01:01");
        Order expectedOrder = new Order(100,new BigDecimal("144.12"), 1, date, 2);
        int searchId = expectedOrder.getId();
        
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        Optional<Order> optionalOrder = orderDao.findOrderById(searchId);
        
        // Assert we did not find the order
        assertFalse("Non Existing order should not be present", optionalOrder.isPresent());
    }
    
        /**
     * Test of getAllOrdersAsList method, of class OrderDaoMongo.
     */
    @Test
    public void testGetAllOrdersAsList() {
        System.out.println("getAllOrdersAsList");
        
        // Get the orderion Collection for easy test verification
        MongoCollection orderCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("order");
        long count = orderCollection.count();
        
        //declare and get the orderlist to be tested
        List<Order> orderList;
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderList = orderDao.getAllOrdersAsList();
        
        // Assert we found the orderList and it is the orderList we expected
        assertEquals("Number of items in the list should equal initial number of orders in database", count, orderList.size());
        assertEquals("First order in the list equals first entry in the database", "230.78" , orderList.get(0).getTotalPrice().toString());
        assertEquals("First order in the list equals first entry in the database", 1, orderList.get(2).getCustomerId());
        assertEquals("First order in the list equals first entry in the database", 2, orderList.get(6).getOrderStatusId());
    }
}
