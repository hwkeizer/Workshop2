/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.controller.PasswordHash;

/**
 *
 * @author thoma
 */
public class DatabaseInit {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInit.class);
    
    private final String dbSettingsFileName = "database_settings.xml";
    private String databaseName;
    private String mySqlConnectionString;
    private String user;
    private String password;

    void installDatabase() {
        readMySqlXML();
        getConnection();
        boolean alreadyExists = checkIfDatabaseAlreadyExists();
        if(alreadyExists){
            log.debug("Database found");
            return;
        }
        
        log.debug("No database found, demo mode assumed");
        initializeDatabase();
        populateDatabase();        
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
            
            node = document.selectSingleNode("/database_settings/databaseName");
            databaseName = node.getText();

            
            mySqlConnectionString = databasePrefix + "://" + serverName + ":" 
                    + portNumber + urlSuffix;
        }
        catch(DocumentException e){
            log.debug("Probleem met het lezen van het configuratie document", e);
        }

        log.debug("XML was read");
    }
    
    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                connection = DriverManager.getConnection(mySqlConnectionString, user, password);
            } catch (SQLException ex) {
                // log an exception for example:
                log.error("Failed to create a single MySQL database connection.", ex);
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            log.error("SQL Driver not found.", ex); 
        }
        
        return connection;
    }
    
    private boolean checkIfDatabaseAlreadyExists() {
        try (Connection connection = getConnection(); 
                ResultSet resultSet = connection.getMetaData().getCatalogs();) {

            //iterate each catalog in the ResultSet
            while (resultSet.next()) {
                // Get the database name, which is at position 1
                String dbName = resultSet.getString(1);
                if(databaseName.equals(dbName)){
                    return true;
                }
            }

        }
        catch(SQLException ex) {
            log.debug("Exception after looking for catalogs");
        }
        return false;
    }

    
    void initializeDatabase() {
        // Prepare the SQL statements to create the DATABASE and recreate it
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        String create_account_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String create_account = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account` (`id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(25) NOT NULL, `password` VARCHAR(180) NOT NULL, `account_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_account_account_type1_idx` (`account_type_id` ASC), UNIQUE INDEX `username_UNIQUE` (`username` ASC), CONSTRAINT `fk_account_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `"+databaseName+"`.`account_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String create_customer = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `first_name` VARCHAR(50) NOT NULL, `last_name` VARCHAR(50) NOT NULL, `ln_prefix` VARCHAR(15) NULL, `account_id` INT NULL, PRIMARY KEY (`id`), INDEX `fk_klant_account1_idx` (`account_id` ASC), CONSTRAINT `fk_klant_account1` FOREIGN KEY (`account_id`) REFERENCES `"+databaseName+"`.`account` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String address_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String address = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address` (`id` INT NOT NULL AUTO_INCREMENT, `street_name` VARCHAR(50) NOT NULL, `number` INT NOT NULL, `addition` VARCHAR(5) NULL, `postal_code` VARCHAR(6) NOT NULL, `city` VARCHAR(45) NOT NULL, `customer_id` INT NOT NULL, `address_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_adres_klant_idx` (`customer_id` ASC), INDEX `fk_adres_adres_type1_idx` (`address_type_id` ASC), CONSTRAINT `fk_adres_klant` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_adres_adres_type1` FOREIGN KEY (`address_type_id`) REFERENCES `"+databaseName+"`.`address_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String order_status = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_status` (`id` INT NOT NULL AUTO_INCREMENT, `status` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String order = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order` (`id` INT NOT NULL AUTO_INCREMENT, `total_price` DECIMAL(6,2) NOT NULL, `customer_id` INT NOT NULL, `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `order_status_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_bestelling_klant1_idx` (`customer_id` ASC),  INDEX `fk_bestelling_bestelling_status1_idx` (`order_status_id` ASC), CONSTRAINT `fk_bestelling_klant1` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestelling_bestelling_status1` FOREIGN KEY (`order_status_id`) REFERENCES `"+databaseName+"`.`order_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String product = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`product` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, `price` DECIMAL(6,2) NOT NULL, `stock` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `name_UNIQUE` (`name` ASC)) ENGINE = InnoDB";
        String order_item = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_item` (`id` INT NOT NULL AUTO_INCREMENT, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+databaseName+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+databaseName+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String trigger = "CREATE DEFINER = CURRENT_USER TRIGGER `"+databaseName+"`.`customer_BEFORE_DELETE` BEFORE DELETE ON `customer` FOR EACH ROW DELETE FROM account WHERE id = OLD.account_id;";
        
        try (Connection connection = getConnection();) {
            Statement stat = connection.createStatement();
            stat.executeUpdate(createDatabase); // Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement. ExecuteQuery kan niet gebruikt worden voor DDL statements
            stat.executeUpdate(create_account_type);
            stat.executeUpdate(create_account);
            stat.executeUpdate(create_customer);
            stat.executeUpdate(address_type);
            stat.executeUpdate(address);            
            stat.executeUpdate(order_status);
            stat.executeUpdate(order);
            stat.executeUpdate(product);
            stat.executeUpdate(order_item);            
            stat.executeUpdate(trigger);
        } catch  (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
        
        log.debug("Database initialized");
    }
    
    void populateDatabase()  {

        // Generate the required password hashes
            String pass1 = PasswordHash.generateHash("welkom");
            String pass2 = PasswordHash.generateHash("welkom");
            String pass3 = PasswordHash.generateHash("welkom");
            String pass4 = PasswordHash.generateHash("geheim");
            String pass5 = PasswordHash.generateHash("welkom");
            String pass6 = PasswordHash.generateHash("welkom");
        // Prepare the SQL statements to insert the test data into the DATABASE
        String insert_account_type = "INSERT INTO `"+databaseName+"`.`account_type`(`id`,`type`) VALUES (1,\"admin\"),(2,\"medewerker\"),(3,\"klant\")";
        String insert_address_type = "INSERT INTO `"+databaseName+"`.`address_type`(`id`,`type`) VALUES (1,\"postadres\"),(2,\"factuuradres\"),(3,\"bezorgadres\")";
        String insert_order_status = "INSERT INTO `"+databaseName+"`.`order_status`(`id`,`status`) VALUES (1,\"nieuw\"),(2,\"in behandeling\"),(3,\"afgehandeld\")";
        String insert_account = "INSERT INTO `"+databaseName+"`.`account`(`id`,`username`,`password`,`account_type_id`) VALUES (1,\"piet\",\""+pass1+"\",1),(2,\"klaas\",\""+pass2+"\",2),(3,\"jan\",\""+pass3+"\",3),(4,\"fred\",\""+pass4+"\",3),(5,\"joost\",\""+pass5+ "\",3),(6,\"jaap\",\""+pass6+"\",3)";
        String insert_customer = "INSERT INTO `"+databaseName+"`.`customer`(`id`,`first_name`,`last_name`,`ln_prefix`,`account_id`) VALUES (1,\"Piet\",\"Pietersen\",null,1), (2,\"Klaas\",\"Klaassen\",null,2),(3,\"Jan\",\"Jansen\",null,3),(4,\"Fred\",\"Boomsma\",null,4),(5,\"Joost\",\"Snel\",null,5)";
        String insert_address = "INSERT INTO `"+databaseName+"`.`address`(`id`,`street_name`,`number`,`addition`,`postal_code`,`city`,`customer_id`,`address_type_id`) VALUES (1,\"Postweg\",201,\"h\",\"3781JK\",\"Aalst\",1,1),(2,\"Snelweg\",56,null,\"3922JL\",\"Ee\",2,1),(3,\"Torenstraat\",82,null,\"7620CX\",\"Best\",2,2),(4,\"Valkstraat\",9,\"e\",\"2424DF\",\"Goorle\",2,3),(5,\"Dorpsstraat\",5,null,\"9090NM\",\"Best\",3,1),(6,\"Plein\",45,null,\"2522BH\",\"Oss\",4,1),(7,\"Maduralaan\",23,null,\"8967HJ\",\"Apeldoorn\",5,1)";
        String insert_order = "INSERT INTO `"+databaseName+"`.`order`(`id`,`total_price`,`customer_id`,`date`,`order_status_id`) VALUES (1,230.78,1,\"2016-01-01 01:01:01\",3),(2,62.97,1,\"2016-05-02 01:01:01\",3),(3,144.12,1,\"2017-03-02 01:01:01\",2),(4,78.23,2,\"2017-04-08 01:01:01\",3),(5,6.45,3,\"2017-06-28 01:01:01\",1),(6,324.65,3,\"2017-06-07 01:01:01\",3),(7,46.08,3,\"2017-07-07 01:01:01\",2),(8,99.56,4,\"2017-07-17 01:01:01\",1),(9,23.23,5,\"2017-07-13 01:01:01\",3)";
        String insert_product = "INSERT INTO `"+databaseName+"`.`product`(`id`,`name`,`price`,`stock`) VALUES (1,\"Goudse belegen kaas\",12.90,134),(2,\"Goudse extra belegen kaas\",14.70,239),(3,\"Leidse oude kaas\",14.65,89),(4,\"Schimmelkaas\",11.74,256),(5,\"Leidse jonge kaas\",11.24,122),(6,\"Boeren jonge kaas\",12.57,85)";
        String insert_order_item = "INSERT INTO `"+databaseName+"`.`order_item`(`id`,`order_id`,`product_id`,`amount`,`subtotal`) VALUES (1,1,6,23,254.12),(2,1,1,26,345.20),(3,1,2,2,24.14),(4,2,1,25,289.89),(5,3,4,2,34.89),(6,4,2,13,156.76),(7,4,5,2,23.78),(8,5,2,2,21.34),(9,6,1,3,35.31),(10,6,3,1,11.23),(11,7,6,1,14.23),(12,7,2,3,31.87),(13,8,4,23,167.32),(14,9,1,1,11.34),(15,9,2,2,22.41)"; 
        try (Connection connection = getConnection();) {
            // Execute the SQL statements to insert the test data into the DATABASE
            Statement stat = connection.createStatement();
            stat.executeUpdate(insert_account_type);
            stat.executeUpdate(insert_address_type);
            stat.executeUpdate(insert_order_status);
            stat.executeUpdate(insert_account);
            stat.executeUpdate(insert_customer);
            stat.executeUpdate(insert_address);
            stat.executeUpdate(insert_order);
            stat.executeUpdate(insert_product);
            stat.executeUpdate(insert_order_item);            
        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
        
        log.debug("Database populated with testdata");
    }
    
}
