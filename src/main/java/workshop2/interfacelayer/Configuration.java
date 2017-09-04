/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hwkei
 */
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    
    private final String dbSettingsFileName = "database_settings.xml";
    private String databaseName;
    private String user;   
    private String password;
    private String databaseType;
    private String persistenceProvider;
    private String mySqlConnectionString;
    private String mongoDbConnectionString;
    
    Configuration() {
        readPersistenceProviderXML();
        readDatabaseTypeXML();
        readMySqlXML();
        readMongoDbXML();
    }
    
    public String getMySqlConnectionString() {
        return mySqlConnectionString;
    }    
        
    public String getMongoDbConnectionString() {
        return mongoDbConnectionString;        
    }
    
    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseType() {
        return databaseType;
    }
 
    public String getPersistenceProvider() {
        return persistenceProvider;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    private void readPersistenceProviderXML(){
        
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName);
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/persistenceProvider");
            persistenceProvider = node.getText();
            
            
            
            log.debug("Default databasetype: {}", persistenceProvider);
            
        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }        
    }    
    
    private void readDatabaseTypeXML(){
        
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName);
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/databaseType");
            databaseType = node.getText();
            
            node = document.selectSingleNode("/database_settings/databaseName");
            databaseName = node.getText();
            
            log.debug("Default databasetype: {}", databaseType);
            
        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }        
    }    
    
    private void readMySqlXML(){
        SAXReader reader = new SAXReader();
        File file = new File(dbSettingsFileName); 
        try {
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/mysql/databasePrefix");
            String databasePrefix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/serverName");
            String serverName = node.getText();            
            
            node = document.selectSingleNode("/database_settings/mysql/urlSuffix");
            String urlSuffix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/portNumber");
            String portNumber = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/user");
            user = node.getText();
            
            node = document.selectSingleNode("/database_settings/mysql/password");
            password = node.getText();
            
            // Compose the MySql connection string from the XML values
            mySqlConnectionString = databasePrefix + "://" + serverName + ":" 
                    + portNumber + "/" + databaseName + urlSuffix;
            log.debug("MySql Connectiestring: {}", mySqlConnectionString);
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
            String databasePrefix = node.getText();
            
            node = document.selectSingleNode("/database_settings/mongodb/serverName");
            String serverName = node.getText();

            node = document.selectSingleNode("/database_settings/mongodb/portNumber");
            String portNumber = node.getText();
            
            // Compose the MongoDB connection string from the XML values
            mongoDbConnectionString = databasePrefix + "://" + serverName + ":" + portNumber;
            log.debug("Mongo Connectiestring: {}", mongoDbConnectionString);
        
        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }        
    }        
}
