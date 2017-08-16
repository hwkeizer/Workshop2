/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.domain.Customer;
import workshop2.interfacelayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.interfacelayer.view.Validator;

/**
 *
 * @author thoma
 */
public class CustomerDaoMysql implements CustomerDao {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerDaoMysql.class);
    
    private static final String SQL_INSERT = "INSERT INTO customer (first_name, last_name, ln_prefix, account_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, first_name, last_name, ln_prefix, account_id FROM customer WHERE last_name = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, first_name, last_name, ln_prefix, account_id FROM customer WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT id, first_name, last_name, ln_prefix, account_id FROM `customer`  ORDER BY last_name ASC";
    private static final String SQL_UPDATE = "UPDATE customer SET first_name=?, last_name=?, ln_prefix=?, account_id=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM customer WHERE id = ?";


    public CustomerDaoMysql() {
    }

    @Override
    public void insertCustomer(Customer customer) {
        try(
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getLastNamePrefix());
            // account id can be null so needs to be checked
            if (customer.getAccountId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, customer.getAccountId());
            }
            
            
            //Execute the prepared statement and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van customer met achternaam {} is mislukt!", customer.getLastName());
            }
            else {
                log.debug("Customer met achternaam {} is toegevoegd", customer.getLastName());
            }
        } catch(SQLException ex){            
            log.error("SQL error: ", ex);
        }
    }
    
    @Override
    public void updateCustomer(Customer customer) {        
                
        // Do nothing if the customer cannot be found in the database
        if ((findCustomerById(customer.getId())) == null) {
            log.error("CustomerId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", customer.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getLastNamePrefix());
            statement.setInt(4, customer.getAccountId());
            statement.setInt(5, customer.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van customer {} is helaas mislukt!", 
                        customer.getLastName());
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }
    
    @Override
    public void deleteCustomer(Customer customer) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setInt(1, customer.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van customer {} is helaas mislukt!", 
                        customer.getLastName());
            } else {
                log.debug("Customer {} is verwijderd uit de database", 
                        customer.getLastName());
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public Optional<Customer> findCustomerById(int customerId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setInt(1, customerId);
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
    public Optional<Customer> findCustomerByAccountId(int accountId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setInt(1, accountId);
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
    public Optional<Customer> findCustomerByLastName(String lastName) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NAME);){
            
            statement.setString(1, lastName);
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
    
    // Helper methode to map the current row of the given ResultSet to a Customer instance
    private Customer map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String lastNamePrefix = resultSet.getString("ln_prefix");
        Integer accountId = resultSet.getInt("account_id");
        return new Customer(id, firstName, lastName, lastNamePrefix, accountId);
    }

    @Override
    public List<Customer> getAllCustomersAsList() {
        List<Customer> customerList = new ArrayList<>();
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);){
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerList.add(map(resultSet));
            }           
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }        
        return customerList;
    }
    
}
