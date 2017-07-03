/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Account;
import domain.Product;
import interfacelayer.DatabaseConnection;
import interfacelayer.DuplicateAccountException;
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
public class AccountDaoMysql implements AccountDao {
    
    private static final Logger log = LoggerFactory.getLogger(AccountDaoMysql.class);
    
    private static final String SQL_INSERT = "INSERT INTO account (username, password, account_type) VALUES (?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, username, password, account_type FROM account WHERE username = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, username, password, account_type FROM account WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE account SET username=?, password=?, account_type=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM account WHERE id = ?";

    @Override
    public void insertAccount(Account account) throws DuplicateAccountException {
    
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {         
            
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, ((Integer)account.getAccountType()).toString());
            
            // Execute the prepared query and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van account {} is mislukt!",
                        account.getUsername());
            } else {
                log.debug("Account toegevoegd: {} {}", account.getUsername());
            }
        } catch (SQLException ex) {
            // If we find errorCode 1062 (duplicate value) we throw that, else we have an unknown ConstraintException and we log that for debugging
            if(ex.getErrorCode() == 1062){
                throw new DuplicateAccountException("Account with name = " + account.getUsername() + " is already in the database");
            } else {
                log.error("SQL error: ", ex);
            }
            
        }        
    }

    @Override
    public void updateAccount(Account account) {
        
        // Do nothing if the product cannot be found in the database
        if ((findAccountById(account.getId())) == null) {
            log.error("Productid '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", account.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, ((Integer)account.getAccountType()).toString());
            statement.setString(4, ((Integer)account.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van account {} is helaas mislukt!", 
                        account.getUsername());
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public void deleteAccount(Account account) {
        try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setString(1, ((Integer)account.getId()).toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van account {} is helaas mislukt!", 
                        account.getUsername());
            } else {
                log.debug("Account {} is verwijderd uit de database", 
                        account.getUsername());
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public Optional<Account> findAccountById(int accountId) {
    try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setString(1, ((Integer)accountId).toString());
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

    
    /**
     * Find a product in the database by name
     * @param username
     * @return Optional<Account>
     */
    @Override
    public Optional<Account> findAccountByUsername(String username) {
            try (
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NAME);){
            
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.ofNullable(map(resultSet));
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        // nothing found
        return null;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Account map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        int accountType = resultSet.getInt("account_type");
        return new Account(id, username, password, accountType);
    }
  
}
