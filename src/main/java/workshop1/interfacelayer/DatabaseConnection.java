/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.dom4j.io.SAXReader;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thoma
 */
public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private String databaseType;
    private String serverName;
    private String databaseName;
    private String urlSuffix;    
    private String portNumber;
    private String user;   
    private String password;
    private String environment;
    
    private DatabaseConnection() {  
        readXML();
    }
    
    private static class SingletonHolder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
    
    public static DatabaseConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (connection != null) {
            log.debug("Database connectie gemaakt");
        }
    }
    
    /**
     * Returns the complete connection string
     * Connection string is composed from the XML configuration values
     * @return 
     */
    private String getConnectionString() {
        return databaseType + "://" + serverName + ":" + portNumber + "/" 
                + databaseName + urlSuffix;
    }
    
    /**
     * Returns a connection to the database
     * @return connection
     */
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                return DriverManager.getConnection(getConnectionString(), user, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                log.error("Failed to create the database connection.", ex);
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            log.error("SQL Driver not found.", ex); 
        }
        return null;
    }
    
    private void readXML(){
        SAXReader reader = new SAXReader();
        // TODO: hard-coded file naam ergens als configuratie of constante zetten
        File file = new File("Database_Settings.xml");
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/databaseType");
            databaseType = node.getText();
            
            node = document.selectSingleNode("/database_settings/serverName");
            serverName = node.getText();
            
            node = document.selectSingleNode("/database_settings/databaseName");
            databaseName = node.getText();
            
            node = document.selectSingleNode("/database_settings/urlSuffix");
            urlSuffix = node.getText();
            
            node = document.selectSingleNode("/database_settings/portNumber");
            portNumber = node.getText();
            
            node = document.selectSingleNode("/database_settings/user");
            user = node.getText();
            
            node = document.selectSingleNode("/database_settings/password");
            password = node.getText();
            
            log.debug("Connectiestring: {}",getConnectionString());

        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }
        
    }
    
}
