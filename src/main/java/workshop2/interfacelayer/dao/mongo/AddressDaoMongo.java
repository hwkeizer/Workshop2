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
import workshop2.domain.Address;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.AddressDao;

/**
 *
 * @author thoma
 */
public class AddressDaoMongo implements AddressDao {
    private static final Logger log = LoggerFactory.getLogger(AddressDaoMongo.class);

    public AddressDaoMongo() {
    }

    @Override
    public void insertAddress(Address address) {
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");        
        
        // Prepare the document to insert
        Document document = new Document("_id", getNextAvailableIndex())
                .append("street_name", address.getStreetName())
                .append("number", address.getNumber())
                .append("addition", address.getAddition())
                .append("postal_code", address.getPostalCode())
                .append("city", address.getCity())
                .append("customer_id", address.getCustomerId().toString())
                .append("address_type_id", address.getAddressTypeId());        

        collection.insertOne(document);
        log.debug("Address toegevoegd: {}", document.toString());
    }

    @Override
    public void updateAddress(Address address) {
        // Do nothing if the address cannot be found in the database
        if ((findAddressById(address.getId())) == null) {
            log.error("AddressId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", address.getId());
            return;
        }
        
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
        
        collection.updateOne(eq("_id", address.getId()), 
                combine(set("street_name", address.getStreetName()),
                        set("number", address.getNumber()),
                        set("addition", address.getAddition()),
                        set("postal_code", address.getPostalCode()),
                        set("city", address.getCity()),
                        set("customer_id", address.getCustomerId()),
                        set("address_type_id", address.getAddressTypeId())));
    }

    @Override
    public void deleteAddress(Address address) {
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
        
        collection.deleteOne(eq("_id", address.getId()));
    }

    @Override
    public Optional<Address> findAddressById(int addressId) {
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
        BasicDBObject query = new BasicDBObject("_id", addressId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public List<Address> findAddressesByCustomerId(int customerId) {
        List<Address> addressList = new ArrayList<>();
         // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
        BasicDBObject query = new BasicDBObject("customer_id", customerId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            addressList.add(map(document));
        }
        return addressList;
    }

    @Override
    public List<String> getAllAddressTypesAsList() {
        List<String> addressTypeList = new ArrayList<>();
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address_type");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            addressTypeList.add(document.getString("type"));
        }
        return addressTypeList;
    }

    @Override
    public List<Address> getAllAddressesAsList() {
        List<Address> addressList = new ArrayList<>();
        // Get the address collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            addressList.add(map(document));
        }
        return addressList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Address instance
    private Address map(Document document) {
        int id = document.getInteger("_id");
        String streetName = document.getString("street_name");
        int number = document.getInteger("number");
        String addition = document.getString("addition");
        String postalCode = document.getString("postal_code");
        String city = document.getString("city");
        int customerId = document.getInteger("customer_id");
        int addressTypeId = document.getInteger("address_type_id");
        return new Address(id, streetName, number, addition, postalCode, city, customerId, addressTypeId);
    }
    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("address");
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
