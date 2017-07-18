/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao;

import workshop1.domain.Order;
import workshop1.interfacelayer.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
public class OrderDaoMysql implements OrderDao {
    
    private static final Logger log = LoggerFactory.getLogger(OrderDaoMysql.class);
    
    // SQL queries for the preparedStatements
    private static final String SQL_INSERT = "INSERT INTO `order` (total_price, customer_id, date, order_status_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `order` SET `total_price`=?, `customer_id`=?, `date`=?,`order_status_id`=? WHERE `id`=?";
    private static final String SQL_FIND_BY_ID = "SELECT id, total_price, customer_id, date, order_status_id FROM `order` WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM `order` WHERE id = ?";

    public OrderDaoMysql() {
    }

    @Override
    public Integer insertOrder(Order order) {
        Integer generatedKey = null;
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);) {         
                        
            statement.setString(1, order.getTotalPrice().toString());
            statement.setString(2, ((Integer)order.getCustomerId()).toString());
            statement.setTimestamp(3, Timestamp.valueOf(order.getDate()));
            statement.setString(4, ((Integer)order.getOrderStatusId()).toString());
            
            // Execute the prepared query and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van de order is mislukt!");
            }
            
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    generatedKey = generatedKeys.getInt(1);
                }
                else
                    generatedKey = null;
            }
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
            
        }
        return generatedKey;
    }

    @Override
    public void updateOrder(Order order) {
        // Do nothing if the product cannot be found in the database
        if ((findOrderById(order.getId())) == null) {
            log.error("Bestelling met id '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", order.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, order.getTotalPrice().toString());
            statement.setString(2, ((Integer)order.getCustomerId()).toString());
            statement.setTimestamp(3, Timestamp.valueOf(order.getDate()));
            statement.setString(4, ((Integer)order.getOrderStatusId()).toString());
            statement.setString(5, ((Integer)order.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van het productis helaas mislukt!");
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public void deleteOrder(Order order) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            statement.setString(1, ((Integer)order.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van het product is helaas mislukt!");
            }   
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public Optional<Order> findOrderById(int orderId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setString(1, ((Integer)orderId).toString());
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
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Order map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        BigDecimal totalPrice = new BigDecimal(resultSet.getString("total_price"));
        int customerId = resultSet.getInt("customer_id");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        int orderStatusId = resultSet.getInt("order_status_id");
        return new Order(id, totalPrice, customerId, date, orderStatusId);
    }
    
}