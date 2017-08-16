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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Order;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.OrderDao;

/**
 *
 * @author thoma
 */
public class OrderDaoMongo implements OrderDao {
    private static final Logger log = LoggerFactory.getLogger(OrderDaoMongo.class);
    
    public OrderDaoMongo() {
    }

    @Override
    public Integer insertOrder(Order order) {
        
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        
        
        int newId = getNextAvailableIndex();
        // Prepare the document to insert
        Document document = new Document("_id", newId)
                .append("total_price", order.getTotalPrice().toString())
                .append("customer_id", order.getCustomerId())
                .append("date", order.getDate().toString())
                .append("order_status_id", order.getOrderStatusId());
        
        collection.insertOne(document);
        log.debug("Order toegevoegd: {}", document.toString());

        return newId;
    }

    @Override
    public void updateOrder(Order order) {
        // Do nothing if the product cannot be found in the database
        if (!findOrderById(order.getId()).isPresent()) {
            log.error("OrderId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", order.getId());
            System.out.println("bestaat niet");
            return;
        }
        
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        
        collection.updateOne(eq("_id", order.getId()), 
                combine(set("total_price", order.getTotalPrice().toString()),
                        set("customer_id", order.getCustomerId()),
                        set("date", order.getDate().toString()),
                        set("order_status_id", order.getOrderStatusId())));
    }

    @Override
    public void deleteOrder(Order order) {
        // Get the order collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        
        collection.deleteOne(eq("_id", order.getId()));
    }

    @Override
    public Optional<Order> findOrderById(int orderId) {
        // Get the order collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        BasicDBObject query = new BasicDBObject("_id", orderId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }
    
    @Override
    public List<Order> getAllOrdersAsList() {
        List<Order> orderList = new ArrayList<>();

        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        
        BasicDBObject query = new BasicDBObject();
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document)cursor.next();  
            orderList.add(Optional.ofNullable(map(document)).get());
        }
        return orderList;
    }
    
    @Override
    public List<Order> getAllOrdersAsListByCustomerId(int customerId) {
        List<Order> orderList = new ArrayList<>();

        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
        
        BasicDBObject query = new BasicDBObject("customer_id", customerId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document)cursor.next();  
            orderList.add(Optional.ofNullable(map(document)).get());
        }
        return orderList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Order map(Document document) {
        int id = document.getInteger("_id");
        BigDecimal totalPrice = new BigDecimal(document.getString("total_price"));
        int customerId = document.getInteger("customer_id");
        LocalDateTime date = LocalDateTime.parse(document.getString("date"));
        int orderStatusId = document.getInteger("order_status_id");
        return new Order(id, totalPrice, customerId, date, orderStatusId);
    }
    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order");
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
