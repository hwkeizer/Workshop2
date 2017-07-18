/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.Product;
import workshop1.interfacelayer.DatabaseConnection;
import workshop1.interfacelayer.dao.DuplicateProductException;
import workshop1.interfacelayer.dao.ProductDao;

/**
 *
 * @author hwkei
 */
public class ProductDaoMongo implements ProductDao {
    private static final Logger log = LoggerFactory.getLogger(ProductDaoMongo.class);

    @Override
    public void insertProduct(Product product) throws DuplicateProductException {
            
        // Prepare the document to insert
        Document document = new Document("name", product.getName())
               .append("price", product.getPrice().toString())
               .append("stock", product.getStock());

        // Get the product collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("product");
        
        // Verify the productname does not exist
        BasicDBObject query = new BasicDBObject("name", product.getName());
        if (collection.find(query).iterator().hasNext()) throw new DuplicateProductException(
                "Product with name = " + product.getName() + " is already in the database");;

        collection.insertOne(document);
        log.debug("Product toegevoegd: {}", document.toString());

    }

    @Override
    public void updateProduct(Product product) {
        // Do nothing if the product cannot be found in the database
        if ((findProductByName(product.getName())) == null) {
            log.error("Productnaam '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", product.getName());
            return;
        }
        
//        try (
//            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
//            PreparedStatement statement = connection.prepareStatement("");) {
//            
//            statement.setString(1, product.getName());
//            statement.setString(2, product.getPrice().toString());
//            statement.setString(3, ((Integer)product.getStock()).toString());
//            statement.setString(4, ((Integer)product.getId()).toString());
//            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                log.error("Het aanpassen van product {} is helaas mislukt!", 
//                        product.getName());
//            }        
//        } catch (SQLException ex) {
//            log.error("SQL error: ", ex);
//        }
    }
    

    @Override
    public void deleteProduct(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Product> getAllProductsAsList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
