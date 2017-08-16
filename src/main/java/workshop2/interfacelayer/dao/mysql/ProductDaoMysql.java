/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.domain.Product;
import workshop2.interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.interfacelayer.dao.ProductDao;

/**
 * MySql implementation of the ProductDao interface
 * @author hwkei
 */
public class ProductDaoMysql implements ProductDao {
    
    private static final Logger log = LoggerFactory.getLogger(ProductDaoMysql.class);
    
    // SQL queries for the preparedStatements
    private static final String SQL_INSERT = "INSERT INTO product (name, price, stock) VALUES (?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, name, price, stock FROM product WHERE name = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, name, price, stock FROM product WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT id, name, price, stock FROM product ORDER BY name asc";
    private static final String SQL_UPDATE = "UPDATE product SET name=?, price=?, stock=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM product WHERE id = ?";
    
    /**
     * Insert a new product in the database.
     * @param product
     * @throws DuplicateProductException
     */
    @Override
    public void insertProduct(Product product) throws DuplicateProductException {
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {         
            
            statement.setString(1, product.getName());
            statement.setString(2, product.getPrice().toString());
            statement.setString(3, ((Integer)product.getStock()).toString());
            
            // Execute the prepared query and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van product {} is mislukt!",
                        product.getName());
            } else {
                log.debug("Product toegevoegd: {} {}", product.getName());
            }
        } catch (SQLException ex) {
            // If we find errorCode 1062 (duplicate value) we throw that, else we have an unknown ConstraintException and we log that for debugging
            if(ex.getErrorCode() == 1062){
                throw new DuplicateProductException("Product with name = " + product.getName() + " is already in the database");
            } else {
                log.error("SQL error: ", ex);
            }
            
        }        
    }
    
    /**
     * Update a product with new value(s). The product id cannot be changed and
     * is used to identify the product
     * @param product
     */
    @Override
    public void updateProduct(Product product) {
        
        // Do nothing if the product cannot be found in the database
        if ((findProductById(product.getId())) == null) {
            log.error("Productid '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", product.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, product.getName());
            statement.setString(2, product.getPrice().toString());
            statement.setString(3, ((Integer)product.getStock()).toString());
            statement.setString(4, ((Integer)product.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van product {} is helaas mislukt!", 
                        product.getName());
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }
    
    /**
     * Delete the given product from the database. The product is identified by
     * the id value.
     * @param product
     */
    @Override
    public void deleteProduct(Product product) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setString(1, ((Integer)product.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van product {} is helaas mislukt!", 
                        product.getName());
            } else {
                log.debug("Product {} is verwijderd uit de database", 
                        product.getName());
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }
    
    /**
     * Find a product in the database by id
     * @param productId
     * @return Optional<Product>
     */
    @Override
    public Optional<Product> findProductById(int productId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setString(1, ((Integer)productId).toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.ofNullable(map(resultSet));
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        // Nothing found
        return Optional.empty(); 
    }
    
    /**
     * Find a product in the database by name
     * @param name
     * @return Optional<Product>
     */
    @Override
    public Optional<Product> findProductByName(String name) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NAME);){
            
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.ofNullable(map(resultSet));
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        // nothing found
        return Optional.empty();
    }
    
    @Override
    public List<Product> getAllProductsAsList(){
        List<Product> productList = new ArrayList<>();
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);){
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productList.add(map(resultSet));
            }           
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        
        return productList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Product map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        BigDecimal price = new BigDecimal(resultSet.getString("price"));
        int stock = resultSet.getInt("stock");
        return new Product(id, name, price, stock);
    }
}
