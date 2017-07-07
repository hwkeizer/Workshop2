/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Customer;
import interfacelayer.DatabaseConnection;
import interfacelayer.DuplicateCustomerException;
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
public class CustomerDaoMysql implements CustomerDao {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerDaoMysql.class);
    
    private static final String SQL_INSERT = "INSERT INTO customer (first_name, last_name, ln_prefix, account_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, first_name, last_name, ln_prefix, account_id FROM customer WHERE last_name = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, first_name, last_name, ln_prefix, account_id FROM customer WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE customer SET first_name=?, last_name=?, ln_prefix=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM customer WHERE id = ?";


    public CustomerDaoMysql() {
    }

    @Override
    public void insertCustomer(Customer customer) throws DuplicateCustomerException {
        log.debug("customer at start of insert customer method " + " " + customer.getFirstName() + " " + customer.getLastName() + " " + customer.getLastNamePrefix() + " " + customer.getAccountId());
        try(
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getLastNamePrefix());
            statement.setInt(4, customer.getAccountId());
            
            //Execute the prepared statement and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van customer met achternaam {} is mislukt!", customer.getLastName());
            }
            else {
                log.debug("Customer met achternaam {} is toegevoegd", customer.getLastName());
            }
        }
        catch(SQLException ex){
            //If we find errorCode 1062 (duplicate value) we throw that, else we have an unknown ConstraintException and we log that for debugging
            if(ex.getErrorCode() == 1062){
                throw new DuplicateCustomerException("Customer with name = " + customer.getFirstName() + " "
                        + customer.getLastNamePrefix() + customer.getLastName() + "is already in the database");
            }
            else {
                log.error("SQL error: ", ex);
            }
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
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getLastNamePrefix());
            statement.setInt(4, customer.getId());
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
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
    public Optional<Customer> findCustomerByLastName(String lastName) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
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
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Customer map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String lastNamePrefix = resultSet.getString("ln_prefix");
        int accountId = resultSet.getInt("account_id");
        log.debug("first name is " + firstName + " and last name is " + lastName);
        return new Customer(id, firstName, lastName, lastNamePrefix, accountId);
    }
    
}
