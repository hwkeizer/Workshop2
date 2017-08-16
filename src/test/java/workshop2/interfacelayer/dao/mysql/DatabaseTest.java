/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.PasswordHash;

/**
 *
 * @author hwkei
 */
public class DatabaseTest {    
    private static final String DATABASE = "applikaasie";
    
    static void initializeDatabase() {
        // Prepare the SQL statements to drop the DATABASE and recreate it
        String dropDatabase = "DROP DATABASE IF EXISTS " + DATABASE;
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DATABASE;
        String create_account_type = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`account_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String create_account = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`account` (`id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(25) NOT NULL, `password` VARCHAR(180) NOT NULL, `account_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_account_account_type1_idx` (`account_type_id` ASC), UNIQUE INDEX `username_UNIQUE` (`username` ASC), CONSTRAINT `fk_account_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `"+DATABASE+"`.`account_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String create_customer = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `first_name` VARCHAR(50) NOT NULL, `last_name` VARCHAR(50) NOT NULL, `ln_prefix` VARCHAR(15) NULL, `account_id` INT NULL, PRIMARY KEY (`id`), INDEX `fk_klant_account1_idx` (`account_id` ASC), CONSTRAINT `fk_klant_account1` FOREIGN KEY (`account_id`) REFERENCES `"+DATABASE+"`.`account` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String address_type = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`address_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String address = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`address` (`id` INT NOT NULL AUTO_INCREMENT, `street_name` VARCHAR(50) NOT NULL, `number` INT NOT NULL, `addition` VARCHAR(5) NULL, `postal_code` VARCHAR(6) NOT NULL, `city` VARCHAR(45) NOT NULL, `customer_id` INT NOT NULL, `address_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_adres_klant_idx` (`customer_id` ASC), INDEX `fk_adres_adres_type1_idx` (`address_type_id` ASC), CONSTRAINT `fk_adres_klant` FOREIGN KEY (`customer_id`) REFERENCES `"+DATABASE+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_adres_adres_type1` FOREIGN KEY (`address_type_id`) REFERENCES `"+DATABASE+"`.`address_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String order_status = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`order_status` (`id` INT NOT NULL AUTO_INCREMENT, `status` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
        String order = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`order` (`id` INT NOT NULL AUTO_INCREMENT, `total_price` DECIMAL(6,2) NOT NULL, `customer_id` INT NOT NULL, `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `order_status_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_bestelling_klant1_idx` (`customer_id` ASC),  INDEX `fk_bestelling_bestelling_status1_idx` (`order_status_id` ASC), CONSTRAINT `fk_bestelling_klant1` FOREIGN KEY (`customer_id`) REFERENCES `"+DATABASE+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestelling_bestelling_status1` FOREIGN KEY (`order_status_id`) REFERENCES `"+DATABASE+"`.`order_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
        String product = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`product` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, `price` DECIMAL(6,2) NOT NULL, `stock` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `name_UNIQUE` (`name` ASC)) ENGINE = InnoDB";
        String order_item = "CREATE TABLE IF NOT EXISTS `"+DATABASE+"`.`order_item` (`id` INT NOT NULL AUTO_INCREMENT, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+DATABASE+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+DATABASE+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
        String trigger = "CREATE DEFINER = CURRENT_USER TRIGGER `"+DATABASE+"`.`customer_BEFORE_DELETE` BEFORE DELETE ON `customer` FOR EACH ROW DELETE FROM account WHERE id = OLD.account_id;";
        
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            Statement stat = connection.createStatement();
            stat.executeUpdate(dropDatabase);
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
    }
    
    static void populateDatabase()  {
        // Generate the required password hashes
            String pass1 = PasswordHash.generateHash("welkom");
            String pass2 = PasswordHash.generateHash("welkom");
            String pass3 = PasswordHash.generateHash("welkom");
            String pass4 = PasswordHash.generateHash("geheim");
            String pass5 = PasswordHash.generateHash("welkom");
            String pass6 = PasswordHash.generateHash("welkom");
        // Prepare the SQL statements to insert the test data into the DATABASE
        String insert_account_type = "INSERT INTO `"+DATABASE+"`.`account_type`(`id`,`type`) VALUES (1,\"admin\"),(2,\"medewerker\"),(3,\"klant\")";
        String insert_address_type = "INSERT INTO `"+DATABASE+"`.`address_type`(`id`,`type`) VALUES (1,\"postadres\"),(2,\"factuuradres\"),(3,\"bezorgadres\")";
        String insert_order_status = "INSERT INTO `"+DATABASE+"`.`order_status`(`id`,`status`) VALUES (1,\"nieuw\"),(2,\"in behandeling\"),(3,\"afgehandeld\")";
        String insert_account = "INSERT INTO `"+DATABASE+"`.`account`(`id`,`username`,`password`,`account_type_id`) VALUES (1,\"piet\",\""+pass1+"\",1),(2,\"klaas\",\""+pass2+"\",2),(3,\"jan\",\""+pass3+"\",3),(4,\"fred\",\""+pass4+"\",3),(5,\"joost\",\""+pass5+ "\",3),(6,\"jaap\",\""+pass6+"\",3)";
        String insert_customer = "INSERT INTO `"+DATABASE+"`.`customer`(`id`,`first_name`,`last_name`,`ln_prefix`,`account_id`) VALUES (1,\"Piet\",\"Pietersen\",null,1), (2,\"Klaas\",\"Klaassen\",null,2),(3,\"Jan\",\"Jansen\",null,3),(4,\"Fred\",\"Boomsma\",null,4),(5,\"Joost\",\"Snel\",null,5)";
        String insert_address = "INSERT INTO `"+DATABASE+"`.`address`(`id`,`street_name`,`number`,`addition`,`postal_code`,`city`,`customer_id`,`address_type_id`) VALUES (1,\"Postweg\",201,\"h\",\"3781JK\",\"Aalst\",1,1),(2,\"Snelweg\",56,null,\"3922JL\",\"Ee\",2,1),(3,\"Torenstraat\",82,null,\"7620CX\",\"Best\",2,2),(4,\"Valkstraat\",9,\"e\",\"2424DF\",\"Goorle\",2,3),(5,\"Dorpsstraat\",5,null,\"9090NM\",\"Best\",3,1),(6,\"Plein\",45,null,\"2522BH\",\"Oss\",4,1),(7,\"Maduralaan\",23,null,\"8967HJ\",\"Apeldoorn\",5,1)";
        String insert_order = "INSERT INTO `"+DATABASE+"`.`order`(`id`,`total_price`,`customer_id`,`date`,`order_status_id`) VALUES (1,230.78,1,\"2016-01-01 01:01:01\",3),(2,62.97,1,\"2016-05-02 01:01:01\",3),(3,144.12,1,\"2017-03-02 01:01:01\",2),(4,78.23,2,\"2017-04-08 01:01:01\",3),(5,6.45,3,\"2017-06-28 01:01:01\",1),(6,324.65,3,\"2017-06-07 01:01:01\",3),(7,46.08,3,\"2017-07-07 01:01:01\",2),(8,99.56,4,\"2017-08-17 01:01:01\",1),(9,23.23,5,\"2017-09-13 01:01:01\",3)";
        String insert_product = "INSERT INTO `"+DATABASE+"`.`product`(`id`,`name`,`price`,`stock`) VALUES (1,\"Goudse belegen kaas\",12.90,134),(2,\"Goudse extra belegen kaas\",14.70,239),(3,\"Leidse oude kaas\",14.65,89),(4,\"Schimmelkaas\",11.74,256),(5,\"Leidse jonge kaas\",11.24,122),(6,\"Boeren jonge kaas\",12.57,85)";
        String insert_order_item = "INSERT INTO `"+DATABASE+"`.`order_item`(`id`,`order_id`,`product_id`,`amount`,`subtotal`) VALUES (1,1,6,23,254.12),(2,1,1,26,345.20),(3,1,2,2,24.14),(4,2,1,25,289.89),(5,3,4,2,34.89),(6,4,2,13,156.76),(7,4,5,2,23.78),(8,5,2,2,21.34),(9,6,1,3,35.31),(10,6,3,1,11.23),(11,7,6,1,14.23),(12,7,2,3,31.87),(13,8,4,23,167.32),(14,9,1,1,11.34),(15,9,2,2,22.41)"; 
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
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
    }
}