/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.DuplicateAccountException;

/**
 *
 * @author hwkei
 */
public class AccountDaoMongo implements AccountDao {
    private static final Logger log = LoggerFactory.getLogger(AccountDaoMongo.class);

    public AccountDaoMongo() {
    }

    @Override
    public void insertAccount(Account account) throws DuplicateAccountException {
        
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        
        // Prepare the document to insert
        Document document = new Document("_id", getNextAvailableIndex())
                .append("username", account.getUsername())
                .append("password", account.getPassword())
                .append("account_type_id", account.getAccountTypeId());
        
         // Verify the accountname does not exist
        BasicDBObject query = new BasicDBObject("username", account.getUsername());
        if (collection.find(query).iterator().hasNext()) throw new DuplicateAccountException(
                "Account with name = " + account.getUsername() + " is already in the database");

        collection.insertOne(document);
        log.debug("Account toegevoegd: {}", document.toString());
    }

    @Override
    public void updateAccount(Account account) {
        
        // Do nothing if the account cannot be found in the database
        if ((findAccountById(account.getId())) == null) {
            log.error("AccountId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", account.getId());
            return;
        }
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        
       collection.updateOne(eq("_id", account.getId()), 
                combine(set("username", account.getUsername()),
                        set("password", account.getPassword()),
                        set("account_type_id", account.getAccountTypeId())));
    }

    @Override
    public void deleteAccount(Account account) {
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        
        collection.deleteOne(eq("_id", account.getId()));
    }

    @Override
    public Optional<Account> findAccountById(int accountId) {
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        BasicDBObject query = new BasicDBObject("_id", accountId);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findAccountByUserName(String userName) {
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        BasicDBObject query = new BasicDBObject("username", userName);
        MongoCursor cursor = collection.find(query).iterator();
        while(cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            return Optional.ofNullable(map(document));
        }
        return Optional.empty();
    }

    @Override
    public List<String> getAllAccountTypesAsList() {
        List<String> accountTypeList = new ArrayList<>();
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account_type");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            accountTypeList.add(document.getString("type"));
        }
        return accountTypeList;
    }

    @Override
    public List<Account> getAllAccountsAsList() {
        List<Account> accountList = new ArrayList<>();
        // Get the account collection
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();  
            accountList.add(map(document));
        }
        return accountList;
    }
    
    // Helper methode to map the current row of the given ResultSet to a Account instance
    private Account map(Document document) {
        int id = document.getInteger("_id");
        String username = document.getString("username");
        String password = document.getString("password");
        int account_type_id = document.getInteger("account_type_id");
        return new Account(id, username, password, account_type_id);
    }
    
    private int getNextAvailableIndex() {    
        MongoDatabase database = DatabaseConnection.getInstance().getMongoDatabase();
        MongoCollection collection = database.getCollection("account");
        int highest = 0;
        MongoCursor cursor = collection.find().iterator();
        while(cursor.hasNext()) {
            Document obj = (Document) cursor.next();  
            int current = obj.getInteger("_id");
            if (highest < current) highest = current;
        }
        return highest + 1;
    }
    
}
