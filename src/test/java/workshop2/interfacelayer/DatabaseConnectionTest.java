/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer;

import workshop2.interfacelayer.DatabaseConnection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hwkei
 */
public class DatabaseConnectionTest {
    
    public DatabaseConnectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getInstance method, of class DatabaseConnection.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        DatabaseConnection result = DatabaseConnection.getInstance();
        assertNotNull("getInstance() should not return null", result);
    }

    /**
     * Test of getMySqlConnection method, of class DatabaseConnection.
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetMySqlConnection() throws SQLException {
        System.out.println("getConnection");
        Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
        assertTrue("New connection should be valid", connection.isValid(0));
        connection.close();
        assertFalse("Closed connection should be invalid", connection.isValid(0)); 
    }
    
     /**
     * Test of getMySqlPoolConnection method, of class DatabaseConnection.
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetMySqlPoolConnection() throws SQLException {
        System.out.println("getConnection");
        DatabaseConnection.getInstance().useConnectionPool(true);
        Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
        assertTrue("New connection should be valid", connection.isValid(0));
        connection.close();
        assertFalse("Closed connection should be invalid", connection.isValid(0)); 
    }
    
    /**
     * Test of getMongoDbClient method, of class DatabaseConnection
     */
    @Test
    public void testGetMongoDbClient() {
        System.out.println("getMongoDbClient");
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();

            for (String db : database.listCollectionNames()) {
                System.out.println(db);
            }
        assertEquals("New connection should return database name", "applikaasie", database.getName());        
    }    
}
