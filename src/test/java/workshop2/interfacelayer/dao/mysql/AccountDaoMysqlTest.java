/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.DuplicateAccountException;
import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Ignore;
import workshop2.interfacelayer.controller.PasswordHash;


/**
 *
 * @author thoma
 */
//@Ignore("Temporary ignore to speed up testing of other DAO's")
public class AccountDaoMysqlTest {
    
    private final int initialNumberOfAccounts = 6; // Initial number of accounts
    
    public AccountDaoMysqlTest() {
    }
    
    @Before
    public void initializeDatabase() {        
        DatabaseConnection.getInstance().setDatabaseType("MYSQL");
        DatabaseTest.initializeDatabase();
        DatabaseTest.populateDatabase();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of insertAccount method, of class AccountDaoMysql.
     * @throws workshop2.interfacelayer.dao.DuplicateAccountException
     */
    @Test
    public void testInsertAccount() throws DuplicateAccountException {
        System.out.println("insertAccount");
        
        //Prepare an account to add to the database
        String testUsername = "dinges";
        String testPassword = "haas";
        testPassword = PasswordHash.generateHash(testPassword);
        Integer testAccountTypeId = 2;
        Account testAccount = new Account(testUsername, testPassword, testAccountTypeId);

        // Count the records before the insert
        int countBefore = getTableCount("account");

        // Add a test account to the database
        //DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();        
        accountDao.insertAccount(testAccount);
        
        // Count the records after the insert and compare with before
        assertEquals("Number of accounts should be increased by one.", countBefore + 1, getTableCount("account"));

        // Try to fetch the account from the database. If it exists and ID is not the same as allready present in database, we have succesfully created a new account
        final String query = "SELECT * FROM `account` WHERE `id` NOT BETWEEN 1 and " + initialNumberOfAccounts + " AND `username`=? AND `password`=? AND `account_type_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testUsername);
            stat.setString(2,testPassword);
            stat.setInt(3,testAccountTypeId);
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue(resultSet.next()); 
                
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals(1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }     
    }
    
    /**
     * Test of insertAccount method, of class AccountDaoMysql.
     * Test if inserting existing account will throw Exception
     */
    @Test
    public void testInsertExistingAccount() {
        System.out.println("insertExistingAccount");
        
        // Prepare a account to add to the database        
        Integer id = 3;
        String testUsername = "jan";
        String testPassword = "welkom";
        testPassword = PasswordHash.generateHash(testPassword);
        Integer testAccountTypeId = 3;
        Account testAccount = new Account(id, testUsername, testPassword, testAccountTypeId);
        
        // Add the prepared account to the database
        try {
            AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
            accountDao.insertAccount(testAccount);
            fail("Adding an existing account should have thrown a DuplicateAccountException");
        } catch (DuplicateAccountException ex) {
            // Assert expected exception
            assertTrue("Exception DuplicateAccountException should be thrown", ex instanceof DuplicateAccountException);
            assertEquals("Exception message should be as expected.", "Account with name = " + testAccount.getUsername() + " is already in the database", ex.getMessage());
        }       
    }

    /**
     * Test of findExistingAccountById method, of class AccountDaoMysql.
     */
    @Test
    public void testFindExistingAccountByUsername() {
        System.out.println("findExistingAccountByUsername");
        
        // Define the account to be searched
        Account expectedAccount = new Account(2, "klaas", "welkom", 2);
        String searchString = expectedAccount.getUsername();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(searchString);
        
        // Assert we found the account and it is the account we expected
        assertTrue("Existing Account should be present", optionalAccount.isPresent());
        assertEquals("Existing Account should be as expected", expectedAccount.getId(), optionalAccount.get().getId());
    }
    
    /**
     * Test of findNonExistingAccountById method, of class AccountDaoMysql.
     */
    @Test
    public void testFindNonExistingAccountByUsername() {
        System.out.println("findNonExistingAccountByUsername");
        
        // Define the account to be searched
        Account expectedAccount = new Account(2, "onbekend", "welkom", 2);
        String searchString = expectedAccount.getUsername();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(searchString);
        
        // Assert we did not find the account
        assertFalse("Non existing Account should not be present", optionalAccount.isPresent());
    }
    
    /**
     * Test of findExistingAccountById method, of class AccountDaoMysql.
     */
    @Test
    public void testFindExistingAccountById() {
        System.out.println("findExistingAccountById");
        
        // Define the account to be searched
        Account expectedAccount = new Account(2, "klaas", "welkom", 2);
        int searchId = expectedAccount.getId();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountById(searchId);
        
        // Assert we found the account and it is the account we expected
        assertTrue("Existing Account should be present", optionalAccount.isPresent());
        assertEquals("Existing Account should be as expected", expectedAccount.getUsername(), optionalAccount.get().getUsername());
    }
    
    /**
     * Test of findNonExistingAccountById method, of class AccountDaoMysql.
     */
    @Test
    public void testFindNonExistingAccountById() {
        System.out.println("findNonExistingAccountById");
        
        // Define the account to be searched
        Account expectedAccount = new Account(20, "klaas", "welkom", 2);
        int searchId = expectedAccount.getId();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountById(searchId);
        
        // Assert we did not find the account
        assertFalse("Non existing Account should not be present", optionalAccount.isPresent());
    }
    
    /**
     * Test of deleteAccount method, of class AccountDaoMysql.
     */
    @Test
    public void testDeleteAccount() {
        System.out.println("deleteAccount");
        
        // Prepare the account to be deleted   
        Integer testId = 3;
        String testUsername = "jan";
        String testPassword = "welkom";
        Integer testAccountTypeId = 3;
        Account testAccount = new Account(testId, testUsername, testPassword, testAccountTypeId);
        
        
        // Try to fetch the account from the database. It must exist or testing will make no sence
        final String query = "SELECT * FROM `account` WHERE `id`=? AND `username`=? AND `account_type_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setInt(1, testId);
            stat.setString(2, testUsername);
            stat.setString(3,testPassword);
            stat.setInt(4,testAccountTypeId);
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue("Account should exist before testing delete", resultSet.next());                 
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals("Account should only exist once before a valid delete test can be done", 1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
        
        // Count the records bedore the deletion
        int countBefore = getTableCount("account");
        
        // Perform the deletion of the account
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();        
        accountDao.deleteAccount(testAccount);
        
        // Count the records after the deletion and compare with before
        assertEquals("Number of accounts should be decreased by one.", countBefore - 1, getTableCount("account"));
        
        // Try to fetch the account from the database. If it does not exist we have succesfully deleted the account
        final String queryForDeleted = "SELECT * FROM `account` WHERE `id`=? AND `username`=? AND `password`=? AND `account_type_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            PreparedStatement stat = connection.prepareStatement(queryForDeleted);
            stat.setString(1, testId.toString());
            stat.setString(2, testUsername);
            stat.setString(3,testPassword);
            stat.setString(4,testAccountTypeId.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }   
    }
    
     /**
     * Helper function to get the number of records from a table
     */
    int getTableCount(String table) {

        final String countQuery = "SELECT COUNT(*) FROM `" + table + "`";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            Statement stat = connection.createStatement();
            try (ResultSet resultSet = stat.executeQuery(countQuery)) {
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }            
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return -1;
    }
    
    /**
     * Test of updateAccount method, of class AccountDaoMysql.
     */
    @Test
    public void testUpdateAccount() {
        System.out.println("updateAccount");
        
        // Prepare a account to add to the database        
        Integer testId = 4;
        String testUsername = "fred";
        String testPassword = "geheim";
        Integer testAccountType = 3;
        Account testAccount = new Account(testId, testUsername, testPassword, testAccountType);
        
        // Set new username, password and account type
        String newUsername = "UpdatedFred";
        String newPassword = "UpdatedGeheim";
        newPassword = PasswordHash.generateHash(newPassword);
        Integer newAccountType = 2;
        
        testAccount.setUsername(newUsername);
        testAccount.setPassword(newPassword);
        testAccount.setAccountType(newAccountType);
        
        // Perform the update in the databse
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountDao.updateAccount(testAccount);
        
        // Validate the update        
        final String query = "SELECT * FROM `account` WHERE `id`=? AND `username`=? AND `password`=? AND `account_type_id`=?";
        try (Connection connection = DatabaseConnection.getInstance().getMySqlConnection();) {
            // Try to find the account with the old values in the database. This should fail
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,testUsername);
            stat.setString(3,testPassword);
            stat.setString(4,testAccountType.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have no matching result
                assertFalse(resultSet.next()); 
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            // Then try to find the account with the new values in the database. This should succeed
            stat = connection.prepareStatement(query);
            stat.setString(1,testId.toString());
            stat.setString(2,newUsername);
            stat.setString(3,newPassword);
            stat.setString(4,newAccountType.toString());
            try (ResultSet resultSet = stat.executeQuery()) {
                // Assert we have at least one matching result
                assertTrue(resultSet.next()); 
                
                // Assert we have at most one matching result
                resultSet.last(); // advance cursor to last row
                assertEquals(1, resultSet.getRow()); // check if it is row number 1
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }     
    }
    
    /**
     * Test of getAllAccountTypesAsList method, of class AccountDaoMysql.
     */
    @Test
    public void testGetAllAccountTypesAsList() {
        System.out.println("getAllAccountTypesAsList");
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        List<String> expectedAccountTypes = new ArrayList<>();
        expectedAccountTypes.add("admin");
        expectedAccountTypes.add("medewerker");
        expectedAccountTypes.add("klant");        
        List<String> allAccountTypes = accountDao.getAllAccountTypesAsList();
        assertEquals("All AccountTypes should be as expected", expectedAccountTypes, allAccountTypes);
        
    }
    
    /**
     * Test of getAllAccountsAsList method, of class AccountDaoMysql.
     */
    @Test
    public void testGetAllAccountsAsList() {
        System.out.println("getAllAccountsAsList");
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        List<Account> accountList;
        accountList = accountDao.getAllAccountsAsList();
        
        String[] expectedNameList = {"fred", "jaap", "jan", "joost", "klaas", "piet"};
        String[] nameList = new String[6]; 
        
        // Assert we found the accountList and it is the accountList we expected
        for (int i=0; i<accountList.size(); i++) {
            nameList[i] = accountList.get(i).getUsername();
        }
        Assert.assertArrayEquals("All Accounts should be as expected", expectedNameList, nameList);
    }        
}
