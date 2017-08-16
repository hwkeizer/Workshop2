/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.PasswordHash;
import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.DuplicateAccountException;

/**
 *
 * @author hwkei
 */
public class AccountDaoMongoTest {
    
    public AccountDaoMongoTest() {
    }
    
    @Before
    public void initializeDatabase() {        
        DatabaseConnection.getInstance().setDatabaseType("MONGO");
        DatabaseTest.initializeMongoDatabase();
        DatabaseTest.populateMongoDatabase();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insertAccount method, of class AccountDaoMongo.
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertAccount() throws Exception {
        // Get the account Collection for easy test verification
        MongoCollection accountCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("account");        

        //Prepare an account to add to the database
        String testUsername = "dinges";
        String testPassword = "haas";
        testPassword = PasswordHash.generateHash(testPassword);
        Integer testAccountTypeId = 2;
        Account testAccount = new Account(testUsername, testPassword, testAccountTypeId);
        
        // Count the records before the insert and verify the account is not yet in the database       
        long countBefore = accountCollection.count();
        BasicDBObject query = new BasicDBObject("username", testUsername);
        assertFalse("Account should not be in database before insertion", accountCollection.find(query).iterator().hasNext());
        
        // Add the prepared account to the database with the DAO
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountDao.insertAccount(testAccount);        
        
        // Verify the records after the insertion and verify the account is inserted
        assertEquals("Number of account should be increased by one.", countBefore + 1, accountCollection.count());
        assertTrue("Account should be in database after insertion", accountCollection.find(query).iterator().hasNext());
    }
    
    /**
     * Test of insertAccount method, of class AccountDaoMongo.
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertExistingAccount() throws Exception {
        // Get the account Collection for easy test verification
        MongoCollection accountCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("account");        

        //Prepare an account to add to the database
        String testUsername = "klaas";
        String testPassword = "Klaassen";
        testPassword = PasswordHash.generateHash(testPassword);
        Integer testAccountTypeId = 2;
        Account testAccount = new Account(testUsername, testPassword, testAccountTypeId);
        
        // Count the records before the insert and verify the account is not yet in the database       
        long countBefore = accountCollection.count();
        BasicDBObject query = new BasicDBObject("username", testUsername);
        assertTrue("Account should be in the database before insertion", accountCollection.find(query).iterator().hasNext());
        
        // Add the prepared account to the database with the DAO
        try {
            AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
            accountDao.insertAccount(testAccount);
            fail("Adding an existing account should have thrown a DuplicateAccountException");
        } catch (DuplicateAccountException ex) {
            // Assert expected exception
            assertTrue("Exception DuplicateAccountException should be thrown", ex instanceof DuplicateAccountException);
            assertEquals("Exception message should be as expected.", "Account with name = " + testAccount.getUsername()+ " is already in the database", ex.getMessage());
        }       
                
        
        // Verify the records after the insertion and verify the account is inserted
        assertEquals("Number of account should be increased by one.", countBefore, accountCollection.count());
        assertTrue("Account should be in database after insertion", accountCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of updateAccount method, of class AccountDaoMongo.
     */
    @Test
    public void testUpdateAccount() {
        System.out.println("updateAccount");
        // Prepare a account to add to the database   
        
        // Get the account Collection for easy test verification
        MongoCollection accountCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("account");
        
        Integer testId = 4;
        String testUsername = "fred";
        String testPassword = "geheim";
        testPassword = PasswordHash.generateHash(testPassword);
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
        
        // Count the number of accounts before the update and verify the old account is and the 
        // new account is not in the database
        long countBefore = accountCollection.count();
        BasicDBObject queryOld = new BasicDBObject("username", testUsername);
        BasicDBObject queryNew = new BasicDBObject("username", newUsername);
        assertTrue("Old Account should be in database before update", accountCollection.find(queryOld).iterator().hasNext());
        assertFalse("New Account should not be in database before update", accountCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountDao.updateAccount(testAccount);
                
        // Validate the old account is not and the new account is in the database and
        // the number of accounts is the same as before
        assertFalse("Old Account should not be in database after update", accountCollection.find(queryOld).iterator().hasNext());
        assertTrue("New Account should be in database after update", accountCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of accounts should not have changed after update", countBefore, accountCollection.count());
    }

    /**
     * Test of deleteAccount method, of class AccountDaoMongo.
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
        
        // Get the accountion Collection for easy test verification
        MongoCollection accountCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("account"); 
        
        // Count the records before the deletion and verify the account is in the database       
        long countBefore = accountCollection.count();
        BasicDBObject query = new BasicDBObject("username", testUsername);
        assertTrue("Account should be in database before deletion", accountCollection.find(query).iterator().hasNext());
        
        // Delete the prepared account from the database with the DAO
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountDao.deleteAccount(testAccount);        
        
        // Verify the records after the insertion and verify the account is deleted
        assertEquals("Number of accounts should be increased by one.", countBefore - 1, accountCollection.count());
        assertFalse("Account should not be in database after deletion", accountCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of findAccountById method, of class AccountDaoMongo.
     */
    @Test
    public void testFindExistingAccountById() {
        System.out.println("findExistingAccountById");
        Account expectedAccount = new Account(2, "klaas", "welkom", 2);
        int searchId = expectedAccount.getId();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountById(searchId);
        
        // Assert we found the account and it is the account we expected
        assertTrue("Existing account should be present", optionalAccount.isPresent());
        assertEquals("Existing account should be the expected account", expectedAccount.getUsername(), optionalAccount.get().getUsername());
    }
    
    /**
     * Test of findAccountById method, of class AccountDaoMongo.
     */
    @Test
    public void testFindNonExistingAccountById() {
        System.out.println("findNonExistingAccountById");
        // Define the account to be searched
        Account expectedAccount = new Account(20, "klaas", "welkom", 2);
        int searchId = expectedAccount.getId();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountById(searchId);
        
        // Assert we found the account and it is the account we expected
        assertFalse("Existing account should be present", optionalAccount.isPresent());
    }


    /**
     * Test of findAccountByUserName method, of class AccountDaoMongo.
     */
    @Test
    public void testFindExistingAccountByUserName() {
        System.out.println("findExistingAccountByUserName");
        // Define the account to be searched
        Account expectedAccount = new Account(2, "klaas", "welkom", 2);
        String searchUserName = expectedAccount.getUsername();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(searchUserName);
        
        // Assert we found the account and it is the account we expected
        assertTrue("Existing account should be present", optionalAccount.isPresent());
        assertEquals("Existing account should be the expected account", expectedAccount.getId(), optionalAccount.get().getId());
    }
    
    /**
     * Test of findAccountByUserName method, of class AccountDaoMongo.
     */
    @Test
    public void testFindNonExistingAccountByUserName() {
        System.out.println("findNonExistingAccountByUserName");
        // Define the account to be searched
        Account expectedAccount = new Account(2, "willempie", "welkom", 2);
        String searchUserName = expectedAccount.getUsername();
        
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(searchUserName);
        
        // Assert the account does not exist
        assertFalse("Existing account should be present", optionalAccount.isPresent());
    }

    /**
     * Test of getAllAccountTypesAsList method, of class AccountDaoMongo.
     */
    @Test
    public void testGetAllAccountTypesAsList() {
        System.out.println("getAllAccountTypesAsList");
        //declare and get the accountlist to be tested
        List<String> expectedAccountTypes = new ArrayList<>();
        List<String> accountTypes;
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountTypes = accountDao.getAllAccountTypesAsList();
        
        expectedAccountTypes.add("admin");
        expectedAccountTypes.add("medewerker");
        expectedAccountTypes.add("klant");      
        
        // Assert we found the accountList and it is the accountList we expected
        assertEquals("All AccountTypes should be as expected", expectedAccountTypes, accountTypes);
    }

    /**
     * Test of getAllAccountsAsList method, of class AccountDaoMongo.
     */
    @Test
    public void testGetAllAccountsAsList() {
        System.out.println("getAllAccountsAsList");
        
        //declare and get the accountlist to be tested
        List<Account> expectedAccounts = new ArrayList<>();
        List<Account> accountList;
        AccountDao accountDao = DaoFactory.getDaoFactory().createAccountDao();
        accountList = accountDao.getAllAccountsAsList();
        
        System.out.println(accountList);
        
        String[] expectedNameList = {"piet", "klaas", "jan", "fred", "joost", "jaap"};
        String[] nameList = new String[6]; 
        
        // Assert we found the accountList and it is the accountList we expected
        for (int i=0; i<accountList.size(); i++) {
            nameList[i] = accountList.get(i).getUsername();
        }
        assertEquals("All Accounts should be as expected", expectedNameList, nameList);
    }
    
}
