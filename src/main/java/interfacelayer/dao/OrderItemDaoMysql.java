/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.OrderItem;
import domain.Product;
import interfacelayer.DatabaseConnection;
import interfacelayer.DuplicateProductException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thoma
 */
public class OrderItemDaoMysql implements OrderItemDao {
    
    private static final Logger log = LoggerFactory.getLogger(OrderItemDaoMysql.class);
    
    // SQL queries for the preparedStatements
    private static final String SQL_INSERT = "INSERT INTO `order_item` (order_id, product_id, amount, subtotal) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `order_item` SET order_id=?, product_id=?, amount=?, subtotal=? WHERE id=?";
    private static final String SQL_FIND_BY_ID = "SELECT id, order_id, product_id, amount, subtotal FROM `order_item` WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM `order_item` WHERE id = ?";

    public OrderItemDaoMysql() {
    }

    @Override
    public void insertOrderItem(OrderItem orderItem) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {         
            
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getAmount());
            statement.setString(4, orderItem.getSubTotal().toString());
            
            // Execute the prepared query and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van een order item is mislukt!");
            }
        } catch (SQLException ex) {            
            log.error("SQL error: ", ex);
            
        }        
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        // Do nothing if the order item cannot be found in the database
        if ((findOrderItemById(orderItem.getId())) == null) {
            log.error("Order item id '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", orderItem.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getAmount());
            statement.setString(4, orderItem.getSubTotal().toString());
            statement.setInt(5, orderItem.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van order item {} is helaas mislukt!", 
                        orderItem.getId());
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setInt(1, orderItem.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van order item {} is helaas mislukt!", 
                        orderItem.getId());
            }           
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public Optional<OrderItem> findOrderItemById(int orderItemId) {
       try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setString(1, ((Integer)orderItemId).toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.ofNullable(map(resultSet));
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        // Nothing found
        return null; 
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private OrderItem map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int orderId = resultSet.getInt("order_id");
        int productId = resultSet.getInt("product_id");
        int amount = resultSet.getInt("amount");
        BigDecimal subTotal = new BigDecimal(resultSet.getString("subtotal"));
        return new OrderItem(id, orderId, productId, amount, subTotal);
    }
    
}
