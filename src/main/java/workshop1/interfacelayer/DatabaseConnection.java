/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
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
    private final String dbSettingsFileName = "Database_Settings.xml";
    private String databasePrefix;
    private String serverName;
    private String databaseName;
    private String urlSuffix;    
    private String portNumber;
    private String user;   
    private String password;
    private MongoClient mongoClient;
    private String databaseType;
    
    private DatabaseConnection() {
        readDatabaseType();
    }
    
    private static class SingletonHolder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
    
    public static DatabaseConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    
    /**
     * Returns the complete connection string
     * Connection string is composed from the XML configuration values
     * @return 
     */
    private String getMySqlConnectionString() {
        return databasePrefix + "://" + serverName + ":" + portNumber + "/" 
                + databaseName + urlSuffix;
    }
    
    private String getMongoDbConnectionString() {
        return databasePrefix + "://" + serverName + ":" + portNumber;
        
    }
    
    public String getDatabaseType() {
        return databaseType;
    }
    
    public void setDatabaseType( String databaseType) {
        this.databaseType = databaseType;
    }
    
    /**
     * Returns a MySql connection to the database
     * @return connection
     */
    public Connection getMySqlConnection(){
        readMySqlXML();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                return DriverManager.getConnection(getMySqlConnectionString(), user, password);
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
    
    public MongoDatabase getMongoDatabase() {
        // TODO: Check existance of database and create it when not available?
        return getMongoDbClient().getDatabase(databaseName);
    }
    
    private MongoClient getMongoDbClient() {
        if (mongoClient != null) return mongoClient;
        readMongoDbXML();
        MongoClientURI uri = new MongoClientURI(getMongoDbConnectionString());
        mongoClient = new MongoClient(uri);
        return mongoClient;
    }
        
    private void readDatabaseType(){
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName);
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/databaseType");
            databaseType = node.getText();
            
        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }
        
    }
    
    private void readMySqlXML(){
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName);
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/mysql/databasePrefix");
            databasePrefix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/serverName");
            serverName = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/databaseName");
            databaseName = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/urlSuffix");
            urlSuffix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/portNumber");
            portNumber = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/user");
            user = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/password");
            password = node.getText();
            
            log.debug("Connectiestring: {}",getMySqlConnectionString());

        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }
        
    }
    
    private void readMongoDbXML() {
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName);
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/mongodb/databasePrefix");
            databasePrefix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mongodb/serverName");
            serverName = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/databaseName");
            databaseName = node.getText();
//            
//            node = document.selectSingleNode("/database_settings/mysql/urlSuffix");
//            urlSuffix = node.getText();
//            
            node = document.selectSingleNode("/database_settings/mongodb/portNumber");
            portNumber = node.getText();
//            
//            node = document.selectSingleNode("/database_settings/mysql/user");
//            user = node.getText();
//            
//            node = document.selectSingleNode("/database_settings/mysql/password");
//            password = node.getText();
            
            log.debug("Connectiestring: {}",getMongoDbConnectionString());

        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }
        
    }
    
}
