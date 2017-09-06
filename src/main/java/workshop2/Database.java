/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import static workshop2.domain.AccountType.*;
import workshop2.domain.Address;
import static workshop2.domain.Address.AddressType.*;
import workshop2.domain.Customer;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import static workshop2.domain.OrderStatus.*;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.PasswordHash;

/**
 *
 * @author thoma
 */
public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    public boolean init() {

        try {            
            // TODO: Voorlopig maken we standaard de testdata aan
            // Spring biedt straks mogelijkheid om test, acceptatie en productie profielen te maken
            initializeDatabase();
        } catch(Exception e) {
            System.out.println("Kan geen verbinding maken met de database: " + e.getStackTrace());
            return false;
        }  
        // TODO: Misschien wat validatie inbouwen, voorlopig even true retour
        return true;        
        
    }
        
    void initializeDatabase() {
        // Quick en Dirty met een eigen EntityManager gedaan
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();          
        EntityTransaction et = em.getTransaction();
        et.begin();

        // Account
        String pass1 = PasswordHash.generateHash("welkom");
        String pass2 = PasswordHash.generateHash("welkom");
        String pass3 = PasswordHash.generateHash("welkom");
        Account account1 = new Account("piet", pass1, ADMIN);
        Account account2 = new Account("klaas", pass2, MEDEWERKER);
        Account account3 = new Account("jan", pass3, KLANT);
        Account account4 = new Account("fred", pass1, KLANT);
        Account account5 = new Account("joost", pass2, KLANT);
        Account account6 = new Account("jaap", pass3, KLANT);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);
        em.persist(account5);
        em.persist(account6);

        // Customer
        Customer customer1 = new Customer("Piet", "Pietersen", null, account1);
        Customer customer2 = new Customer("Klaas", "Klaassen", "van", account2);
        Customer customer3 = new Customer("Jan", "Jansen", null, account3);
        Customer customer4 = new Customer("Fred", "Horst", "ter", account4);
        Customer customer5 = new Customer("Joost", "Draaier", "den", account5);
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        em.persist(customer4);
        em.persist(customer5);
        
        // Address
        Address address1 = new Address("Postweg", 201, "h", "3781JK", "Aalst", customer1, POSTADRES);
        Address address2 = new Address("Snelweg", 56, null, "3922JL", "Ee", customer2, POSTADRES);
        Address address3 = new Address("Torenstraat", 82, null, "7620CX", "Best", customer2, FACTUURADRES);
        Address address4 = new Address("Valkstraat", 9, "e", "2424DF", "Goorle", customer2, BEZORGADRES);
        Address address5 = new Address("Dorpsstraat", 5, null, "9090NM", "Best", customer3, POSTADRES);
        Address address6 = new Address("Plein", 45, null, "2522BH", "Oss", customer4, POSTADRES);
        Address address7 = new Address("Maduralaan", 23, null, "8967HJ", "Apeldoorn", customer5, POSTADRES);
        em.persist(address1);
        em.persist(address2);
        em.persist(address3);
        em.persist(address4);
        em.persist(address5);
        em.persist(address6);
        em.persist(address7);
        
        // Order
        Order order1 = new Order(new BigDecimal("230.78"), customer1, LocalDateTime.now(), AFGEHANDELD);
        Order order2 = new Order(new BigDecimal("62.97"), customer1, LocalDateTime.now(), AFGEHANDELD);
        Order order3 = new Order(new BigDecimal("144.12"), customer1, LocalDateTime.now(), IN_BEHANDELING);
        Order order4 = new Order(new BigDecimal("78.23"), customer2, LocalDateTime.now(), AFGEHANDELD);
        Order order5 = new Order(new BigDecimal("6.45"), customer3, LocalDateTime.now(), NIEUW);
        Order order6 = new Order(new BigDecimal("324.65"), customer3, LocalDateTime.now(), AFGEHANDELD);
        Order order7 = new Order(new BigDecimal("46.08"), customer3, LocalDateTime.now(), IN_BEHANDELING);
        Order order8 = new Order(new BigDecimal("99.56"), customer4, LocalDateTime.now(), NIEUW);
        Order order9 = new Order(new BigDecimal("23.23"), customer5, LocalDateTime.now(), AFGEHANDELD);
        em.persist(order1);
        em.persist(order2);
        em.persist(order3);
        em.persist(order4);
        em.persist(order5);
        em.persist(order6);
        em.persist(order7);
        em.persist(order8);
        em.persist(order9);

        // Product
        Product product1 = new Product("Goudse belegen kaas", new BigDecimal("12.99"), 134);
        Product product2 = new Product("Goudse extra belegen kaas", new BigDecimal("14.70"), 239);
        Product product3 = new Product("Leidse oude kaas", new BigDecimal("14.65"), 89);
        Product product4 = new Product("Schimmelkaas", new BigDecimal("11.74"), 256);
        Product product5 = new Product("Leidse jonge kaas", new BigDecimal("11.24"), 122);
        Product product6 = new Product("Boeren jonge kaas", new BigDecimal("12.57"), 85);
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);
        em.persist(product4);
        em.persist(product5);
        em.persist(product6);
        
        
        // OrderItem
        OrderItem orderItem1 = new OrderItem(order1, product6, 23, new BigDecimal("254.12"));
        OrderItem orderItem2 = new OrderItem(order1, product1, 26, new BigDecimal("345.20"));
        OrderItem orderItem3 = new OrderItem(order1, product2, 2, new BigDecimal("24.14"));
        OrderItem orderItem4 = new OrderItem(order2, product1, 25, new BigDecimal("289.89"));
        OrderItem orderItem5 = new OrderItem(order3, product4, 2, new BigDecimal("34.89"));
        OrderItem orderItem6 = new OrderItem(order4, product2, 13, new BigDecimal("156.76"));
        OrderItem orderItem7 = new OrderItem(order4, product5, 2, new BigDecimal("23.78"));
        OrderItem orderItem8 = new OrderItem(order5, product2, 2, new BigDecimal("21.34"));
        OrderItem orderItem9 = new OrderItem(order6, product1, 3, new BigDecimal("35.31"));
        OrderItem orderItem10 = new OrderItem(order6, product3, 1, new BigDecimal("11.23"));
        OrderItem orderItem11 = new OrderItem(order7, product6, 1, new BigDecimal("14.23"));
        OrderItem orderItem12 = new OrderItem(order7, product2, 3, new BigDecimal("31.87"));
        OrderItem orderItem13 = new OrderItem(order8, product4, 23, new BigDecimal("167.32"));
        OrderItem orderItem14 = new OrderItem(order9, product1, 1, new BigDecimal("11.34"));
        OrderItem orderItem15 = new OrderItem(order9, product2, 2, new BigDecimal("22.41"));
        em.persist(orderItem1);
        em.persist(orderItem2);
        em.persist(orderItem3);
        em.persist(orderItem4);
        em.persist(orderItem5);
        em.persist(orderItem6);
        em.persist(orderItem7);
        em.persist(orderItem8);
        em.persist(orderItem9);
        em.persist(orderItem10);
        em.persist(orderItem11);
        em.persist(orderItem12);
        em.persist(orderItem13);
        em.persist(orderItem14);
        em.persist(orderItem15);
        
        
        et.commit();
        
        // Close the persistency context
        em.close();
    }
//        // Prepare the SQL statements to create the DATABASE and recreate it
//        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + databaseName;
//        String create_account_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String create_account = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`account` (`id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(25) NOT NULL, `password` VARCHAR(180) NOT NULL, `account_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_account_account_type1_idx` (`account_type_id` ASC), UNIQUE INDEX `username_UNIQUE` (`username` ASC), CONSTRAINT `fk_account_account_type1` FOREIGN KEY (`account_type_id`) REFERENCES `"+databaseName+"`.`account_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String create_customer = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `first_name` VARCHAR(50) NOT NULL, `last_name` VARCHAR(50) NOT NULL, `ln_prefix` VARCHAR(15) NULL, `account_id` INT NULL, PRIMARY KEY (`id`), INDEX `fk_klant_account1_idx` (`account_id` ASC), CONSTRAINT `fk_klant_account1` FOREIGN KEY (`account_id`) REFERENCES `"+databaseName+"`.`account` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String address_type = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address_type` (`id` INT NOT NULL AUTO_INCREMENT, `type` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String address = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`address` (`id` INT NOT NULL AUTO_INCREMENT, `street_name` VARCHAR(50) NOT NULL, `number` INT NOT NULL, `addition` VARCHAR(5) NULL, `postal_code` VARCHAR(6) NOT NULL, `city` VARCHAR(45) NOT NULL, `customer_id` INT NOT NULL, `address_type_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_adres_klant_idx` (`customer_id` ASC), INDEX `fk_adres_adres_type1_idx` (`address_type_id` ASC), CONSTRAINT `fk_adres_klant` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_adres_adres_type1` FOREIGN KEY (`address_type_id`) REFERENCES `"+databaseName+"`.`address_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String order_status = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_status` (`id` INT NOT NULL AUTO_INCREMENT, `status` VARCHAR(45) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB";
//        String order = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order` (`id` INT NOT NULL AUTO_INCREMENT, `total_price` DECIMAL(6,2) NOT NULL, `customer_id` INT NOT NULL, `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `order_status_id` INT NOT NULL, PRIMARY KEY (`id`), INDEX `fk_bestelling_klant1_idx` (`customer_id` ASC),  INDEX `fk_bestelling_bestelling_status1_idx` (`order_status_id` ASC), CONSTRAINT `fk_bestelling_klant1` FOREIGN KEY (`customer_id`) REFERENCES `"+databaseName+"`.`customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestelling_bestelling_status1` FOREIGN KEY (`order_status_id`) REFERENCES `"+databaseName+"`.`order_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String product = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`product` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, `price` DECIMAL(6,2) NOT NULL, `stock` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `name_UNIQUE` (`name` ASC)) ENGINE = InnoDB";
//        String order_item = "CREATE TABLE IF NOT EXISTS `"+databaseName+"`.`order_item` (`id` INT NOT NULL AUTO_INCREMENT, `order_id` INT NOT NULL, `product_id` INT NULL, `amount` INT NOT NULL, `subtotal` DECIMAL(6,2) NOT NULL, INDEX `fk_bestel_regel_bestelling1_idx` (`order_id` ASC), INDEX `fk_bestel_regel_artikel1_idx` (`product_id` ASC), PRIMARY KEY (`id`), CONSTRAINT `fk_bestel_regel_bestelling1` FOREIGN KEY (`order_id`) REFERENCES `"+databaseName+"`.`order` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION, CONSTRAINT `fk_bestel_regel_artikel1` FOREIGN KEY (`product_id`) REFERENCES `"+databaseName+"`.`product` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION) ENGINE = InnoDB";
//        String trigger = "CREATE DEFINER = CURRENT_USER TRIGGER `"+databaseName+"`.`customer_BEFORE_DELETE` BEFORE DELETE ON `customer` FOR EACH ROW DELETE FROM account WHERE id = OLD.account_id;";
//        
//        try (Connection connection = getConnection();) {
//            Statement stat = connection.createStatement();
//            stat.executeUpdate(createDatabase); // Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement. ExecuteQuery kan niet gebruikt worden voor DDL statements
//            stat.executeUpdate(create_account_type);
//            stat.executeUpdate(create_account);
//            stat.executeUpdate(create_customer);
//            stat.executeUpdate(address_type);
//            stat.executeUpdate(address);            
//            stat.executeUpdate(order_status);
//            stat.executeUpdate(order);
//            stat.executeUpdate(product);
//            stat.executeUpdate(order_item);            
//            stat.executeUpdate(trigger);
//        } catch  (SQLException ex) {
//            System.out.println("SQLException" + ex);
//        }
//        
//        log.debug("Database initialized");
//    }
//    
//    void populateDatabase()  {
//
//        // Generate the required password hashes
//            String pass1 = PasswordHash.generateHash("welkom");
//            String pass2 = PasswordHash.generateHash("welkom");
//            String pass3 = PasswordHash.generateHash("welkom");
//            String pass4 = PasswordHash.generateHash("geheim");
//            String pass5 = PasswordHash.generateHash("welkom");
//            String pass6 = PasswordHash.generateHash("welkom");
//        // Prepare the SQL statements to insert the test data into the DATABASE
//        String insert_account_type = "INSERT INTO `"+databaseName+"`.`account_type`(`id`,`type`) VALUES (1,\"admin\"),(2,\"medewerker\"),(3,\"klant\")";
//        String insert_address_type = "INSERT INTO `"+databaseName+"`.`address_type`(`id`,`type`) VALUES (1,\"postadres\"),(2,\"factuuradres\"),(3,\"bezorgadres\")";
//        String insert_order_status = "INSERT INTO `"+databaseName+"`.`order_status`(`id`,`status`) VALUES (1,\"nieuw\"),(2,\"in behandeling\"),(3,\"afgehandeld\")";
//        String insert_account = "INSERT INTO `"+databaseName+"`.`account`(`id`,`username`,`password`,`account_type_id`) VALUES (1,\"piet\",\""+pass1+"\",1),(2,\"klaas\",\""+pass2+"\",2),(3,\"jan\",\""+pass3+"\",3),(4,\"fred\",\""+pass4+"\",3),(5,\"joost\",\""+pass5+ "\",3),(6,\"jaap\",\""+pass6+"\",3)";
//        String insert_customer = "INSERT INTO `"+databaseName+"`.`customer`(`id`,`first_name`,`last_name`,`ln_prefix`,`account_id`) VALUES (1,\"Piet\",\"Pietersen\",null,1), (2,\"Klaas\",\"Klaassen\",null,2),(3,\"Jan\",\"Jansen\",null,3),(4,\"Fred\",\"Boomsma\",null,4),(5,\"Joost\",\"Snel\",null,5)";
//        String insert_address = "INSERT INTO `"+databaseName+"`.`address`(`id`,`street_name`,`number`,`addition`,`postal_code`,`city`,`customer_id`,`address_type_id`) VALUES (1,\"Postweg\",201,\"h\",\"3781JK\",\"Aalst\",1,1),(2,\"Snelweg\",56,null,\"3922JL\",\"Ee\",2,1),(3,\"Torenstraat\",82,null,\"7620CX\",\"Best\",2,2),(4,\"Valkstraat\",9,\"e\",\"2424DF\",\"Goorle\",2,3),(5,\"Dorpsstraat\",5,null,\"9090NM\",\"Best\",3,1),(6,\"Plein\",45,null,\"2522BH\",\"Oss\",4,1),(7,\"Maduralaan\",23,null,\"8967HJ\",\"Apeldoorn\",5,1)";
//        String insert_order = "INSERT INTO `"+databaseName+"`.`order`(`id`,`total_price`,`customer_id`,`date`,`order_status_id`) VALUES (1,230.78,1,\"2016-01-01 01:01:01\",3),(2,62.97,1,\"2016-05-02 01:01:01\",3),(3,144.12,1,\"2017-03-02 01:01:01\",2),(4,78.23,2,\"2017-04-08 01:01:01\",3),(5,6.45,3,\"2017-06-28 01:01:01\",1),(6,324.65,3,\"2017-06-07 01:01:01\",3),(7,46.08,3,\"2017-07-07 01:01:01\",2),(8,99.56,4,\"2017-07-17 01:01:01\",1),(9,23.23,5,\"2017-07-13 01:01:01\",3)";
//        String insert_product = "INSERT INTO `"+databaseName+"`.`product`(`id`,`name`,`price`,`stock`) VALUES (1,\"Goudse belegen kaas\",12.90,134),(2,\"Goudse extra belegen kaas\",14.70,239),(3,\"Leidse oude kaas\",14.65,89),(4,\"Schimmelkaas\",11.74,256),(5,\"Leidse jonge kaas\",11.24,122),(6,\"Boeren jonge kaas\",12.57,85)";
//        String insert_order_item = "INSERT INTO `"+databaseName+"`.`order_item`(`id`,`order_id`,`product_id`,`amount`,`subtotal`) VALUES (1,1,6,23,254.12),(2,1,1,26,345.20),(3,1,2,2,24.14),(4,2,1,25,289.89),(5,3,4,2,34.89),(6,4,2,13,156.76),(7,4,5,2,23.78),(8,5,2,2,21.34),(9,6,1,3,35.31),(10,6,3,1,11.23),(11,7,6,1,14.23),(12,7,2,3,31.87),(13,8,4,23,167.32),(14,9,1,1,11.34),(15,9,2,2,22.41)"; 
//        try (Connection connection = getConnection();) {
//            // Execute the SQL statements to insert the test data into the DATABASE
//            Statement stat = connection.createStatement();
//            stat.executeUpdate(insert_account_type);
//            stat.executeUpdate(insert_address_type);
//            stat.executeUpdate(insert_order_status);
//            stat.executeUpdate(insert_account);
//            stat.executeUpdate(insert_customer);
//            stat.executeUpdate(insert_address);
//            stat.executeUpdate(insert_order);
//            stat.executeUpdate(insert_product);
//            stat.executeUpdate(insert_order_item);            
//        } catch (SQLException ex) {
//            System.out.println("SQLException" + ex);
//        }
//        
//        log.debug("Database populated with testdata");
//    }
    
}
