/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thoma
 */
public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private final Configuration configuration;
    private String databaseType;
    private String persistenceProvider;
    private boolean useConnectionPool;
    private HikariConfig config;
    private HikariDataSource ds;
    private MongoClient mongoClient;
    // New field for Hibernate
    private final EntityManagerFactory entityManagerFactory;
    
    private DatabaseConnection() {
        // The default configuration is created
        configuration = new Configuration();
        databaseType = configuration.getDatabaseType();
        persistenceProvider = configuration.getPersistenceProvider();
        // Added for Hibernate
        entityManagerFactory = Persistence.createEntityManagerFactory("Hibernate");
    }    

    private static class SingletonHolder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
    
    public static DatabaseConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void closeDatabase() {
        entityManagerFactory.close();
    }
    
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    /**
     * Set the database type to be used
     * This will overrule the default setting from the XML configuration!
     * @param databaseType 
     */
    public void setDatabaseType( String databaseType) {
        if (this.databaseType.equals(databaseType)) return; // No change in database type
        log.debug("Switching databasetype to {}", databaseType);
        this.databaseType = databaseType;
        // If we do not use MySQL then turn off the connectionpool
        if (!databaseType.equals("MYSQL")) useConnectionPool(false); 
    }
    
    /**
     * Get the current active database type
     * @return 
     */
    public String getDatabaseType() {
        return this.databaseType;
    }

    public String getPersistenceProvider() {
        return persistenceProvider;
    }

    public void setPersistenceProvider(String persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }
    
    /**
     * Get the current pool setting
     */
    public boolean getUseConnectionPool() {
        return useConnectionPool;
    }
    
    /**
     * Switch to turn on or off the MySQL connection pool
     * @param useConnectionPool 
     */
    public void useConnectionPool(boolean useConnectionPool) {
        this.useConnectionPool = useConnectionPool;
        if (useConnectionPool) {
            if (ds == null) {
                config = new HikariConfig();
                config.setJdbcUrl(configuration.getMySqlConnectionString());
                config.setUsername(configuration.getUser());
                config.setPassword(configuration.getPassword());
                config.addDataSourceProperty("cachePrepStmts" , "true");
                config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
                config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
                ds = new HikariDataSource(config);
            }
        } else {
            if (ds != null) {
                ds.close();
            }
        }
    }
    
    /**
     * Returns a single or pooled MySql connection depending on the useConnectionPool boolean
     * @return connection
     */
    public Connection getMySqlConnection(){
        if (useConnectionPool) {
            // Use connectionpool
            log.debug("Mysql connection pool active");
            try {
                return ds.getConnection();
            } catch (SQLException ex) {
                // log an exception for example:
                    log.error("Failed to get database connection from the connection pool", ex);
            }
            
        } else {
            // use single MySqlConnection
            log.debug("Mysql single connection active");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try {
                    return DriverManager.getConnection(configuration.getMySqlConnectionString(), 
                            configuration.getUser(), 
                            configuration.getPassword());
                } catch (SQLException ex) {
                    // log an exception for example:
                    log.error("Failed to create a single MySQL database connection.", ex);
                }
            } catch (ClassNotFoundException ex) {
                // log an exception. for example:
                log.error("SQL Driver not found.", ex); 
            }            
        }
        return null;
    }
    
    public MongoDatabase getMongoDatabase() {
        // TODO: Check existance of database and create it when not available?
        if (mongoClient == null) {
            MongoClientURI uri = new MongoClientURI(configuration.getMongoDbConnectionString());
            mongoClient = new MongoClient(uri);
        }
        return mongoClient.getDatabase(configuration.getDatabaseName());
    }
}