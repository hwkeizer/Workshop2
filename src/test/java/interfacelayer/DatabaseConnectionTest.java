/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer;

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
     * Test of getConnection method, of class DatabaseConnection.
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        System.out.println("getConnection");
        Connection connection = DatabaseConnection.getInstance().getConnection();
        assertTrue("New connection should be valid", connection.isValid(0));
        connection.close();
        assertFalse("Closed connection should be invalid", connection.isValid(0)); 
    }
    
}
