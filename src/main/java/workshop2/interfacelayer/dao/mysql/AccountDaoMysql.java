/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.DuplicateAccountException;

/**
 *
 * @author thoma
 */
public class AccountDaoMysql implements AccountDao {
    
    private static final Logger log = LoggerFactory.getLogger(AccountDaoMysql.class);
    
    private static final String SQL_INSERT = "INSERT INTO account (username, password, account_type_id) VALUES (?, ?, ?)";
    private static final String SQL_FIND_BY_NAME = "SELECT id, username, password, account_type_id FROM account WHERE username = ?";
    private static final String SQL_FIND_BY_ID = "SELECT id, username, password, account_type_id FROM account WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE account SET username=?, password=?, account_type_id=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM account WHERE id = ?";
    private static final String SQL_LIST_ALL_ACCOUNTTYPES = "SELECT `type` FROM `account_type`";
    private static final String SQL_LIST_ALL_ACCOUNTS = "SELECT `id`,`username`,`password`,`account_type_id` FROM `account` ORDER BY username ASC";

    @Override
    public void insertAccount(Account account) throws DuplicateAccountException {
    
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {         
            
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setInt(3, account.getAccountTypeId());
            
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
        
        // Do nothing if the account cannot be found in the database
        if ((findAccountById(account.getId())) == null) {
            log.error("AccountId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", account.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, ((Integer)account.getAccountTypeId()).toString());
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
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setInt(1, account.getId());
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
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
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
        return Optional.empty(); 
    }

    
    /**
     * Find a account in the database by name
     * @param username
     * @return Optional<Account>
     */
    @Override
    public Optional<Account> findAccountByUserName(String username) {
            try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
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
        return Optional.empty();
    }
    
    // Helper methode to map the current row of the given ResultSet to a Product instance
    private Account map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        int accountType = resultSet.getInt("account_type_id");
        return new Account(id, username, password, accountType);
    }

    @Override
    public List<String> getAllAccountTypesAsList() {
        List<String> allAccountTypes = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            Statement statement = connection.createStatement();            
            ResultSet resultSet = statement.executeQuery(SQL_LIST_ALL_ACCOUNTTYPES);            
            while (resultSet.next()) {
                allAccountTypes.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return allAccountTypes;
    }

    @Override
    public List<Account> getAllAccountsAsList() {
        List<Account> allAccounts = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            Statement statement = connection.createStatement();            
            ResultSet resultSet = statement.executeQuery(SQL_LIST_ALL_ACCOUNTS);            
            while (resultSet.next()) {
                allAccounts.add(new Account(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)));
            }
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return allAccounts;
    }
  
}
