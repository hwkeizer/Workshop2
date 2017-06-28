/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer;

import domain.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
public class ProductDaoMysql implements ProductDao {
    
    private static final Logger log = LoggerFactory.getLogger(ProductDaoMysql.class);
    
    // SQL queries for the preparedStatements
    private static final String SQL_INSERT = "INSERT INTO product (name, price, stock) VALUES (?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, name, price, stock FROM product WHERE name = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, name, price, stock FROM product WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE product SET name=?, price=?, stock=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM product WHERE id = ?";
    
    @Override
    public void insertProduct(Product product) {
        
        // Do nothing if the product name already exists in the database
        if (findProductByName(product.getName()) != null) {
            log.error("Productnaam '{}' bestaat al in de database en kan niet "
                    + "nogmaals worden toegevoegd!", product.getName());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
            log.error("SQL error: ", ex);
        }        
    }
    
    @Override
    public void updateProduct(Product product) {
        
        // Do nothing if the product cannot be found in the database
        if ((findProductById(product.getId())) == null) {
            log.error("Productid '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", product.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
    
    @Override
    public void deleteProduct(Product product) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
    
    @Override
    public Product findProductById(int productId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setString(1, ((Integer)productId).toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return map(resultSet);
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return null; 
    }
    
    @Override
    public Product findProductByName(String name) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NAME);){
            
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return map(resultSet);
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return null;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Product map(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(new BigDecimal(resultSet.getString("price")));
        product.setStock(resultSet.getInt("stock"));
        return product;
    }
}
