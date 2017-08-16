/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.CustomerDao;

/**
 *
 * @author thoma
 */
public class CustomerDaoMongo implements CustomerDao {
    private static final Logger log = LoggerFactory.getLogger(CustomerDaoMongo.class);

    public CustomerDaoMongo() {
    }

    @Override
    public void insertCustomer(Customer customer) {
        // Get the customer collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        
        // Prepare the document to insert
        Document document = new Document("_id", getNextAvailableIndex())
                .append("first_name", customer.getFirstName())
                .append("last_name", customer.getLastName())
                .append("ln_prefix", customer.getLastNamePrefix())
                .append("account_id", customer.getAccountId());
        

        collection.insertOne(document);
        log.debug("Customer toegevoegd: {}", document.toString());
    }

    @Override
    public void updateCustomer(Customer customer) {
        // Do nothing if the customer cannot be found in the database
        System.out.println(customer.getId());
        if ((findCustomerById(customer.getId())) == null) {
            log.error("CustomerId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", customer.getId());
            return;
        }
        // Get the customer collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        
       collection.updateOne(eq("_id", customer.getId()), 
                combine(set("first_name", customer.getFirstName()),
                        set("last_name", customer.getLastName()),
                        set("ln_prefix", customer.getLastNamePrefix()),
                        set("account_id", customer.getAccountId())));
    }

    @Override
    public void deleteCustomer(Customer customer) {
         // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        
        collection.deleteOne(eq("_id", customer.getId()));
    }

    @Override
    public Optional<Customer> findCustomerById(int customerId) {
        // Get the customer collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        BasicDBObject query = new BasicDBObject("_id", customerId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findCustomerByAccountId(int accountId) {
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        BasicDBObject query = new BasicDBObject("account_id", accountId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findCustomerByLastName(String lastName) {
        // Get the customer collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        BasicDBObject query = new BasicDBObject("last_name", lastName);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public List<Customer> getAllCustomersAsList() {
        List<Customer> customerList = new ArrayList<>();
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            customerList.add(map(document));
        }
        return customerList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Account instance
    private Customer map(Document document) {
        int id = document.getInteger("_id");
        String firstName = document.getString("first_name");
        String lastName = document.getString("last_name");
        String lastNamePrefix = document.getString("ln_prefix");
        Integer account_id = document.getInteger("account_id");
        return new Customer(id, firstName, lastName, lastNamePrefix, account_id);
    }
    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("customer");
        int highest = 0;
        MongoCursor cursor = collection.find().iterator();
        while(cursor.hasNext()) {
            Document obj = (Document) cursor.next();  
            int current = obj.getInteger("_id");
            if (highest < current) highest = current;
        }
        return highest + 1;
    }
    
}
