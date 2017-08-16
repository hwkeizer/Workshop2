/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderDao;
import workshop2.domain.Order;
import workshop2.interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class OrderDaoMysqlTest {
    private static final Logger log = LoggerFactory.getLogger(OrderDaoMysqlTest.class);
    private final int initialNumberOfOrders = 9; // Initial number of products
    
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
     * Test of insertOrder method, of class OrderDaoMysql.
     */
    @Test
    public void testInsertOrder() {
        System.out.println("insertOrder");
        
        // Prepare an order to add to the database        
        BigDecimal testTotalPrice = new BigDecimal("13.55");
        Integer testCustomerId = 3;
        Integer year = 2016;
        Integer month = 8;
        Integer day = 22;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 2;
        Order testOrder = new Order(testTotalPrice, testCustomerId, testDate, testOrderStatusId); 
        
        // Count the records before the insert
        int countBefore = getTableCount("order");
        
        // Add the prepared order to the database
        OrderDao orderDAO = DaoFactory.getDaoFactory().createOrderDao();
        orderDAO.insertOrder(testOrder);        
        
        // Count the records after the insert and compare with before
        assertEquals("Number of orders should be increased by one.", countBefore + 1, getTableCount("order"));
        
        // Try to fetch the order from the database. If it exists and ID is not the same as allready present in database, we have succesfully created a new order
        final String query = "SELECT * FROM `order` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfOrders + " AND `total_price`=? AND `customer_id`=? AND year(date)=? AND month(date)=? AND day(date)=? AND `order_status_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testTotalPrice.toString());
            stat.setString(2,testCustomerId.toString());
            stat.setString(3, year.toString());
            stat.setString(4, month.toString());
            stat.setString(5, day.toString());
            stat.setString(6,testOrderStatusId.toString());
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
    
    @Test
    public void testInsertOrderReturnsNewlyGeneratedId() {
        System.out.println("testInsertOrderReturnsNewlyGeneratedId");
        
        // Prepare an order to add to the database
        BigDecimal testTotalPrice = new BigDecimal("13.55");
        Integer testCustomerId = 3;
        Integer year = 2016;
        Integer month = 8;
        Integer day = 22;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 2;
        Order testOrder = new Order(testTotalPrice, testCustomerId, testDate, testOrderStatusId);
        
        // Add the prepared order to the database and return the key
        OrderDao orderDAO = DaoFactory.getDaoFactory().createOrderDao();
        int returnedKey = orderDAO.insertOrder(testOrder);
        
        //retrieve the object using the returned key
        Order retrievedOrder = orderDAO.findOrderById(returnedKey).get();
        
        //test if the objects are the same
        log.debug("tesOrder is:\n" + testOrder.toString());
        log.debug("retrievedOrder is:\n" + retrievedOrder.toString());
        
        assertTrue("Existing found order should be as expected", testOrder.equalsNoId(retrievedOrder));
        
//        assertEquals("Existing found order should be as expected", testOrder.getTotalPrice(), retrievedOrder.getTotalPrice());
//        assertEquals("Existing found order should be as expected", testOrder.getCustomerId(), retrievedOrder.getCustomerId());
//        assertEquals("Existing found order should be as expected", testOrder.getDate().toLocalDate(), retrievedOrder.getDate().toLocalDate());
//        assertEquals("Existing found order should be as expected", testOrder.getOrderStatusId(), retrievedOrder.getOrderStatusId());
        
    }
    
    @Test
    public void testUpdateOrder() {
        System.out.println("updateOrder");
        
        // Prepare a product to update in the database
        Integer testId = 2;
        BigDecimal testTotalPrice = new BigDecimal("62.97");
        Integer testCustomerId = 1;
        Integer year = 2016;
        Integer month = 5;
        Integer day = 2;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 3;
        
        Order testOrder = new Order(testId, testTotalPrice, testCustomerId, testDate, testOrderStatusId);
        
        // Set a new price, customer_id, date and order_status_id
        BigDecimal newTotalPrice = new BigDecimal("52.97");
        Integer newCustomerId = 2;
        Integer newYear = 2017;
        Integer newMonth = 5;
        Integer newDay = 2;
        LocalDateTime newDate = LocalDate.of(newYear, newMonth, newDay).atTime(LocalTime.now());
        Integer newOrderStatusId = 3;
        
        testOrder.setTotalPrice(newTotalPrice);
        testOrder.setCustomerId(newCustomerId);
        testOrder.setDate(newDate);
        testOrder.setOrderStatusId(newOrderStatusId);
        
        // Perform the update in the databse
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderDao.updateOrder(testOrder);
                
        // Validate the update        
        final String query = "SELECT * FROM `order` WHERE `id`=? AND `total_price`=? AND `customer_id`=? AND year(date)=? AND month(date)=? AND day(date)=? AND `order_status_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            // Try to find the order with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,testTotalPrice.toString());
            stat.setString(3,testCustomerId.toString());
            stat.setString(4, year.toString());
            stat.setString(5, month.toString());
            stat.setString(6, day.toString());
            stat.setString(7,testOrderStatusId.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the order with the new values in the database. This should succeed
            stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,newTotalPrice.toString());
            stat.setString(3,newCustomerId.toString());
            stat.setString(4, newYear.toString());
            stat.setString(5, newMonth.toString());
            stat.setString(6, newDay.toString());
            stat.setString(7,newOrderStatusId.toString());
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
     * Test of deleteOrder method, of class OrderDaoMysql.
     */
    @Test
    public void testDeleteOrder() {
        System.out.println("deleteOrder");
        
        // Prepare an order to delete from the database 
        Integer testId = 7;
        BigDecimal testTotalPrice = new BigDecimal("46.08");
        Integer testCustomerId = 3;
        Integer year = 2017;
        Integer month = 7;
        Integer day = 7;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Integer testOrderStatusId = 2;
        Order testOrder = new Order(testId, testTotalPrice, testCustomerId, testDate, testOrderStatusId); 
        
        // Try to fetch the product from the database. It must exist or testing will make no sence
        final String query = "SELECT * FROM `order` WHERE `id`=? AND `total_price`=? AND `customer_id`=? AND year(date)=? AND month(date)=? AND day(date)=? AND `order_status_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,testTotalPrice.toString());
            stat.setString(3,testCustomerId.toString());
            stat.setString(4, year.toString());
            stat.setString(5, month.toString());
            stat.setString(6, day.toString());
            stat.setString(7,testOrderStatusId.toString());
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
        
        // Count the records before the deletion
        int countBefore = getTableCount("order");
        
        // Delete the prepared order from the database
        OrderDao orderDAO = DaoFactory.getDaoFactory().createOrderDao();
        orderDAO.deleteOrder(testOrder);        
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of orders should be decreased by one.", countBefore - 1, getTableCount("order"));        
        
        // Try to fetch the product from the database. If it does not exist we have succesfully deleted the product
        final String queryForDeleted = "SELECT * FROM `order` WHERE `id`=? AND `total_price`=? AND `customer_id`=? AND year(date)=? AND month(date)=? AND day(date)=? AND `order_status_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setString(1,testId.toString());
            stat.setString(2,testTotalPrice.toString());
            stat.setString(3,testCustomerId.toString());
            stat.setString(4, year.toString());
            stat.setString(5, month.toString());
            stat.setString(6, day.toString());
            stat.setString(7,testOrderStatusId.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }            
        }  catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }  
    }
    
    /**
     * Test of findExistingOrderById method, of class OrderDaoMysql.
     */
    @Test
    public void testFindExistingOrderById() {
        System.out.println("findExistingOrderById");
        
        // Define the product to be searched
        Integer year = 2017;
        Integer month = 4;
        Integer day = 8;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Order expectedOrder = new Order(4, new BigDecimal("78.23"), 2, testDate, 3);
        int searchId = expectedOrder.getId();
        
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        Optional<Order> optionalOrder = orderDao.findOrderById(searchId);
        
        // Assert we found the product and it is the product we expected
        assertTrue("Existing order should be present", optionalOrder.isPresent());
        assertEquals("Existing found order should be as expected", expectedOrder, optionalOrder.get());
    }
    
    /**
     * Test of findNonExistingOrderById method, of class OrderDaoMysql.
     */
    @Test
    public void testFindNonExistingOrderById() {
        System.out.println("findNonExistingOrderById");
        
        // Define the product to be searched
        Integer year = 2017;
        Integer month = 4;
        Integer day = 8;
        LocalDateTime testDate = LocalDate.of(year,month,day).atTime(LocalTime.now());
        Order expectedOrder = new Order(40, new BigDecimal("78.23"), 2, testDate, 3);
        int searchId = expectedOrder.getId();
        
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        Optional<Order> optionalOrder = orderDao.findOrderById(searchId);
        
        // Assert we did not find the order
        assertFalse("Non existing order should not be present", optionalOrder.isPresent());
    }
    
    /**
     * Test of getAllOrdersAsList method, of class OrderDaoMysql.
     */
    @Test
    public void testgetAllOrdersAsList() {
        System.out.println("getAllProductsAsList");
        
        //declare and get the productlist to be tested
        List<Order> orderList = new ArrayList<>();
        OrderDao orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderList = orderDao.getAllOrdersAsList();
        

        
        // Assert we found the productList and it is the productList we expected
        assertEquals("Number of items in the list should equal initial number of products in database", initialNumberOfOrders, orderList.size());
        assertEquals("First product in the list equals first entry in the database", 3, orderList.get(0).getOrderStatusId());
        assertEquals("Second product in the list equals second entry in the database", "62.97" , orderList.get(1).getTotalPrice().toString());
        assertEquals("Third product in the list equals third entry in the database", 2 , orderList.get(2).getOrderStatusId());

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
}