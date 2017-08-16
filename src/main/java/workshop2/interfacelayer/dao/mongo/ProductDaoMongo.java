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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.interfacelayer.dao.ProductDao;

/**
 *
 * @author hwkei
 */
public class ProductDaoMongo implements ProductDao {
    private static final Logger log = LoggerFactory.getLogger(ProductDaoMongo.class);

    @Override
    public void insertProduct(Product product) throws DuplicateProductException {

        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");        
        
        // Prepare the document to insert
        Document document = new Document("_id", getNextAvailableIndex())
                .append("name", product.getName())
                .append("price", product.getPrice().toString())
                .append("stock", product.getStock());
        
        // Verify the productname does not exist
        BasicDBObject query = new BasicDBObject("name", product.getName());
        if (collection.find(query).iterator().hasNext()) throw new DuplicateProductException(
                "Product with name = " + product.getName() + " is already in the database");

        collection.insertOne(document);
        log.debug("Product toegevoegd: {}", document.toString());

    }

    @Override
    public void updateProduct(Product product) {
        // Do nothing if the product cannot be found in the database
        if ((findProductById(product.getId())) == null) {
            log.error("ProductId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", product.getId());
            return;
        }
        
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        
        collection.updateOne(eq("_id", product.getId()), 
                combine(set("name", product.getName()),
                        set("price", product.getPrice().toString()),
                        set("stock", product.getStock())));
        
    }
    

    @Override
    public void deleteProduct(Product product) {
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        
        collection.deleteOne(eq("_id", product.getId()));
        
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        BasicDBObject query = new BasicDBObject("_id", productId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        BasicDBObject query = new BasicDBObject("name", name);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAllProductsAsList() {
        List<Product> productList = new ArrayList<>();
        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            productList.add(map(document));
        }
        return productList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Product map(Document document) {
        int id = document.getInteger("_id");
        String name = document.getString("name");
        BigDecimal price = new BigDecimal(document.getString("price"));
        int stock = document.getInteger("stock");
        return new Product(id, name, price, stock);
    }
    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
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
