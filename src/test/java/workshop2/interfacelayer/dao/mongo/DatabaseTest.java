/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.bson.Document;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.PasswordHash;

/**
 *
 * @author hwkei
 */
public class DatabaseTest {
     private static final String DATABASE = "applikaasie";
     
     static void initializeMongoDatabase() {        
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        // Drop the database
        database.drop();
        // And recreate it again
        database.createCollection("account_type");
        database.createCollection("account");
        database.createCollection("customer");
        database.createCollection("address_type");
        database.createCollection("address");
        database.createCollection("order_status");
        database.createCollection("order");
        database.createCollection("product");
        database.createCollection("order_item");            
    }
    
    static void populateMongoDatabase() {         
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection<Document> collection = database.getCollection("account_type");
        // Account type
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("type", "admin")),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("type", "medewerker")),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("type", "klant"))));

        // Address type
        collection = database.getCollection("address_type");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("type", "postadres")),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("type", "factuuradres")),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("type", "bezorgadres"))));

         // Order status
        collection = database.getCollection("order_status");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("status", "nieuw")),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("status", "in behandeling")),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("status", "afgehandeld"))));  

        // Account
        // Generate the required password hashes
        String pass1 = PasswordHash.generateHash("welkom");
        String pass2 = PasswordHash.generateHash("welkom");
        String pass3 = PasswordHash.generateHash("welkom");
        String pass4 = PasswordHash.generateHash("geheim");
        String pass5 = PasswordHash.generateHash("welkom");
        String pass6 = PasswordHash.generateHash("welkom");
        collection = database.getCollection("account");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("username", "piet")
                    .append("password", pass1)
                    .append("account_type_id", 1)),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("username", "klaas")
                    .append("password", pass2)
                    .append("account_type_id", 2)),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("username", "jan")
                    .append("password", pass3)
                    .append("account_type_id", 3)),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("username", "fred")
                    .append("password", pass4)
                    .append("account_type_id", 3)),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("username", "joost")
                    .append("password", pass5)
                    .append("account_type_id", 3)),
            new InsertOneModel<>(new Document("_id", 6)
                    .append("username", "jaap")
                    .append("password", pass6)
                    .append("account_type_id", 3))));

        // Customer
        collection = database.getCollection("customer");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("first_name", "Piet")
                    .append("last_name", "Pietersen")
                    .append("ln_prefix", "van")
                    .append("account_id", 1)),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("first_name", "Klaas")
                    .append("last_name", "Klaassen")
                    .append("ln_prefix", null)
                    .append("account_id", 2)),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("first_name", "Jan")
                    .append("last_name", "Jansen")
                    .append("ln_prefix", null)
                    .append("account_id", 3)),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("first_name", "Fred")
                    .append("last_name", "Boomsma")
                    .append("ln_prefix", null)
                    .append("account_id", 4)),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("first_name", "Joost")
                    .append("last_name", "Snel")
                    .append("ln_prefix", null)
                    .append("account_id", 5))));

        // Address
        collection = database.getCollection("address");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("street_name", "Postweg")
                    .append("number", 201)
                    .append("addition", "h")
                    .append("postal_code", "3781JK")
                    .append("city", "Aalst")
                    .append("customer_id", 1)
                    .append("address_type_id", 1)),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("street_name", "Snelweg")
                    .append("number", 56)
                    .append("postal_code", "3922JL")
                    .append("city", "Ee")
                    .append("customer_id", 2)
                    .append("address_type_id", 1)),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("street_name", "Torenstraat")
                    .append("number", 82)
                    .append("postal_code", "7620CX")
                    .append("city", "Best")
                    .append("customer_id", 2)
                    .append("address_type_id", 2)),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("street_name", "Valkstraat")
                    .append("number", 9)
                    .append("addition", "e")
                    .append("postal_code", "2424DF")
                    .append("city", "Goorle")
                    .append("customer_id", 2)
                    .append("address_type_id", 3)),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("street_name", "Dorpsstraat")
                    .append("number", 5)
                    .append("postal_code", "9090NM")
                    .append("city", "Best")
                    .append("customer_id", 3)
                    .append("address_type_id", 1)),
            new InsertOneModel<>(new Document("_id", 6)
                    .append("street_name", "Plein")
                    .append("number", 45)
                    .append("postal_code", "2522BH")
                    .append("city", "Oss")
                    .append("customer_id", 4)
                    .append("address_type_id", 1)),
            new InsertOneModel<>(new Document("_id", 7)
                    .append("street_name", "Maduralaan")
                    .append("number", 23)
                    .append("postal_code", "8967HJ")
                    .append("city", "Apeldoorn")
                    .append("customer_id", 5)
                    .append("address_type_id", 1))));

        // Order
        collection = database.getCollection("order");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("total_price", "230.78")
                    .append("customer_id", 1)
                    .append("date", "2016-01-01T01:01:01")
                    .append("order_status_id", 3)),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("total_price", "62.97")
                    .append("customer_id", 1)
                    .append("date", "2016-05-02T01:01:01")
                    .append("order_status_id", 3)),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("total_price", "144.12")
                    .append("customer_id", 1)
                    .append("date", "2017-06-02T01:01:01")
                    .append("order_status_id", 2)),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("total_price", "78.23")
                    .append("customer_id", 2)
                    .append("date", "2017-04-08T01:01:01")
                    .append("order_status_id", 3)),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("total_price", "6.45")
                    .append("customer_id", 3)
                    .append("date", "2017-06-28T01:01:01")
                    .append("order_status_id", 1)),
            new InsertOneModel<>(new Document("_id", 6)
                    .append("total_price", "324.65")
                    .append("customer_id", 3)
                    .append("date", "2017-06-07T01:01:01")
                    .append("order_status_id", 3)),
            new InsertOneModel<>(new Document("_id", 7)
                    .append("total_price", "46.08")
                    .append("customer_id", 3)
                    .append("date", "2017-06-07T01:01:01")
                    .append("order_status_id", 2)),
            new InsertOneModel<>(new Document("_id", 8)
                    .append("total_price", "99.56")
                    .append("customer_id", 4)
                    .append("date", "2017-06-17T01:01:01")
                    .append("order_status_id", 1)),
            new InsertOneModel<>(new Document("_id", 9)
                    .append("total_price", "23.23")
                    .append("customer_id", 5)
                    .append("date", "2017-05-13T01:01:01")
                    .append("order_status_id", 3))));

        // Product
        collection = database.getCollection("product");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("name", "Goudse belegen kaas")
                    .append("price", "12.90")
                    .append("stock", 134)),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("name", "Goudse extra belegen kaas")
                    .append("price", "14.70")
                    .append("stock", 239)),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("name", "Leidse oude kaas")
                    .append("price", "14.65")
                    .append("stock", 89)),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("name", "Schimmelkaas")
                    .append("price", "11.74")
                    .append("stock", 256)),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("name", "Leidse jonge kaas")
                    .append("price", "11.24")
                    .append("stock", 122)),
            new InsertOneModel<>(new Document("_id", 6)
                    .append("name", "Boeren jonge kaas")
                    .append("price", "12.57")
                    .append("stock", 85))));

        // Order item
        collection = database.getCollection("order_item");
        collection.bulkWrite(Arrays.asList(
            new InsertOneModel<>(new Document("_id", 1)
                    .append("order_id", 1)
                    .append("product_id", 6)
                    .append("amount", 23)
                    .append("subtotal", "254.12")),
            new InsertOneModel<>(new Document("_id", 2)
                    .append("order_id", 1)
                    .append("product_id", 1)
                    .append("amount", 26)
                    .append("subtotal", "345.20")),
            new InsertOneModel<>(new Document("_id", 3)
                    .append("order_id", 1)
                    .append("product_id", 2)
                    .append("amount", 2)
                    .append("subtotal", "24.14")),
            new InsertOneModel<>(new Document("_id", 4)
                    .append("order_id", 2)
                    .append("product_id", 1)
                    .append("amount", 25)
                    .append("subtotal", "289.89")),
            new InsertOneModel<>(new Document("_id", 5)
                    .append("order_id", 3)
                    .append("product_id", 4)
                    .append("amount", 2)
                    .append("subtotal", "34.89")),
            new InsertOneModel<>(new Document("_id", 6)
                    .append("order_id", 4)
                    .append("product_id", 2)
                    .append("amount", 13)
                    .append("subtotal", "156.76")),
            new InsertOneModel<>(new Document("_id", 7)
                    .append("order_id", 4)
                    .append("product_id", 5)
                    .append("amount", 2)
                    .append("subtotal", "23.78")),
            new InsertOneModel<>(new Document("_id", 8)
                    .append("order_id", 5)
                    .append("product_id", 2)
                    .append("amount", 2)
                    .append("subtotal", "21.34")),
            new InsertOneModel<>(new Document("_id", 9)
                    .append("order_id", 6)
                    .append("product_id", 1)
                    .append("amount", 3)
                    .append("subtotal", "35.31")),
            new InsertOneModel<>(new Document("_id", 10)
                    .append("order_id", 6)
                    .append("product_id", 3)
                    .append("amount", 1)
                    .append("subtotal", "11.23")),
            new InsertOneModel<>(new Document("_id", 11)
                    .append("order_id", 7)
                    .append("product_id", 6)
                    .append("amount", 1)
                    .append("subtotal", "14.23")),
            new InsertOneModel<>(new Document("_id", 12)
                    .append("order_id", 7)
                    .append("product_id", 2)
                    .append("amount", 3)
                    .append("subtotal", "31.87")),
            new InsertOneModel<>(new Document("_id", 13)
                    .append("order_id", 8)
                    .append("product_id", 4)
                    .append("amount", 23)
                    .append("subtotal", "167.32")),
            new InsertOneModel<>(new Document("_id", 14)
                    .append("order_id", 9)
                    .append("product_id", 1)
                    .append("amount", 1)
                    .append("subtotal", "11.34")),
            new InsertOneModel<>(new Document("_id", 15)
                    .append("order_id", 9)
                    .append("product_id", 2)
                    .append("amount", 2)
                    .append("subtotal", "22.41"))));
        
    }        
    
}
