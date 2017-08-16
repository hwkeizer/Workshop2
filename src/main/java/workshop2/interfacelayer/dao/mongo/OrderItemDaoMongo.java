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
import workshop2.domain.OrderItem;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.interfacelayer.dao.OrderItemDao;

/**
 *
 * @author thoma
 */
public class OrderItemDaoMongo implements OrderItemDao {
    private static final Logger log = LoggerFactory.getLogger(OrderItemDaoMongo.class);
    
    public OrderItemDaoMongo() {
    }

    @Override
    public void insertOrderItem(OrderItem orderItem) {
        
        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
        
        // Prepare the document to insert
        Document document = new Document("_id", getNextAvailableIndex())
                .append("order_id", orderItem.getOrderId())
                .append("product_id", orderItem.getProductId())
                .append("amount", orderItem.getAmount())
                .append("subtotal", orderItem.getSubTotal().toString());
        
        

        collection.insertOne(document);
        log.debug("Product toegevoegd: {}", document.toString());
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        // Do nothing if the product cannot be found in the database
        if (!findOrderItemById(orderItem.getId()).isPresent()) {
            log.error("OrderItemId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", orderItem.getId());
            return;
        }
        
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
        
        collection.updateOne(eq("_id", orderItem.getId()), 
                combine(set("order_id", orderItem.getOrderId()),
                        set("product_id", orderItem.getProductId()),
                        set("amount", orderItem.getAmount()),
                        set("subtotal", orderItem.getSubTotal().toString())));
    }
    

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
        
        collection.deleteOne(eq("_id", orderItem.getId()));
    }

    @Override
    public Optional<OrderItem> findOrderItemById(int orderItemId) {
        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
        BasicDBObject query = new BasicDBObject("_id", orderItemId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAllOrderItemsAsListByOrderId(Integer orderId) {
        List<OrderItem> orderItemList = new ArrayList<>();

        // Get the orderItem collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
        
        BasicDBObject query = new BasicDBObject("order_id", orderId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document)cursor.next();  
            orderItemList.add(Optional.ofNullable(map(document)).get());
        }
        return orderItemList;
    }
    
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private OrderItem map(Document document) {
        int id = document.getInteger("_id");
        int orderId = document.getInteger("order_id");
        int productId = document.getInteger("product_id");
        int amount = document.getInteger("amount");
        BigDecimal subTotal = new BigDecimal(document.getString("subtotal"));
        return new OrderItem(id, orderId, productId, amount, subTotal);
        
    }

    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("order_item");
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
