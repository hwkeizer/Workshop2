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
import workshop2.domain.Address;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.AddressDao;
import workshop2.interfacelayer.dao.DaoFactory;

/**
 *
 * @author hwkei
 */
public class AddressDaoMongoTest {
    
    public AddressDaoMongoTest() {
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
     * Test of insertAddress method, of class AddressDaoMongo.
     */
    @Test
    public void testInsertAddress() {
        System.out.println("insertAddress");
        // Get the addression Collection for easy test verification
        MongoCollection addressCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("address");        

        //Prepare an address to add to the database
        String testStreetName = "dinges";
        Integer testNumber = 5;
        String testAddition = "b";
        String testPostalCode = "2546CH";
        String testCity = "Utrecht";
        Integer testCustomerId = 5;
        Integer testAddressTypeId = 2;
        Address testAddress = new Address(testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);
        
        // Count the records before the insert and verify the address is not yet in the database       
        long countBefore = addressCollection.count();
        BasicDBObject query = new BasicDBObject("street_name", testStreetName);
        assertFalse("Address should not be in database before insertion", addressCollection.find(query).iterator().hasNext());
        
        // Add the prepared address to the database with the DAO
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        addressDao.insertAddress(testAddress);        
        
        // Verify the records after the insertion and verify the address is inserted
        assertEquals("Number of addresss should be increased by one.", countBefore + 1, addressCollection.count());
        assertTrue("Address should be in database after insertion", addressCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of updateAddress method, of class AddressDaoMongo.
     */
    @Test
    public void testUpdateAddress() {
        System.out.println("updateAddress");
        
        // Get the address Collection for easy test verification
        MongoCollection addressCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("address");
        
         //Prepare an address to be updated     
        Integer testId = 6;
        String testStreetName = "Plein";
        Integer testNumber = 45;
        String testAddition = null;
        String testPostalCode = "2522BH";
        String testCity = "Oss";
        Integer testCustomerId = 4;
        Integer testAddressTypeId = 1;
        Address testAddress = new Address(testId, testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);

        // Set new username, password and address type
        int newNumber = 6;
        String newAddition = "bis";
        String newStreetName = "Naamloos";
        String newPostalCode = "1234AB";
        String newCity = "Verweggistan";
        Integer newAddressTypeId = 1;
        
        testAddress.setNumber(newNumber);
        testAddress.setAddition(newAddition);
        testAddress.setStreetName(newStreetName);
        testAddress.setPostalCode(newPostalCode);
        testAddress.setCity(newCity);
        testAddress.setAddressTypeId(newAddressTypeId);
        
        // Count the number of products before the update and verify the old address is and the 
        // new address is not in the database
        long countBefore = addressCollection.count();
        BasicDBObject queryOld = new BasicDBObject("street_name", testStreetName);
        BasicDBObject queryNew = new BasicDBObject("street_name", newStreetName);
        assertTrue("Old Address should be in database before update", addressCollection.find(queryOld).iterator().hasNext());
        assertFalse("New Address should not be in database before update", addressCollection.find(queryNew).iterator().hasNext());
        
        // Perform the update in the databse
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        addressDao.updateAddress(testAddress);
                
        // Validate the old address is not and the new address is in the database and
        // the number of addresss is the same as before
        assertFalse("Old Address should not be in database after update", addressCollection.find(queryOld).iterator().hasNext());
        assertTrue("New Address should be in database after update", addressCollection.find(queryNew).iterator().hasNext());
        assertEquals("Number of addresses should not have changed after update", countBefore, addressCollection.count());
    }

    /**
     * Test of deleteAddress method, of class AddressDaoMongo.
     */
    @Test
    public void testDeleteAddress() {
        System.out.println("deleteAddress");
        // Prepare the address to be deleted   
        Integer testId = 6;
        String testStreetName = "Plein";
        Integer testNumber = 45;
        String testAddition = null;
        String testPostalCode = "2522BH";
        String testCity = "Oss";
        Integer testCustomerId = 4;
        Integer testAddressTypeId = 1;
        Address testAddress = new Address(testId, testStreetName, testNumber, testAddition, testPostalCode, testCity, testCustomerId, testAddressTypeId);
        
        // Get the production Collection for easy test verification
        MongoCollection addressCollection = DatabaseConnection.getInstance().getMongoDatabase().getCollection("address"); 
        
        // Count the records before the deletion and verify the address is in the database       
        long countBefore = addressCollection.count();
        BasicDBObject query = new BasicDBObject("street_name", testStreetName);
        assertTrue("Address should be in database before deletion", addressCollection.find(query).iterator().hasNext());
        
        // Delete the prepared address from the database with the DAO
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        addressDao.deleteAddress(testAddress);        
        
        // Verify the records after the insertion and verify the address is deleted
        assertEquals("Number of addresss should be increased by one.", countBefore - 1, addressCollection.count());
        assertFalse("Address should not be in database after deletion", addressCollection.find(query).iterator().hasNext());
    }

    /**
     * Test of findAddressById method, of class AddressDaoMongo.
     */
    @Test
    public void testFindExistingAddressById() {
        System.out.println("findExistingAddressById");
        
        // Define the address to be searched
        Address expectedAddress = new Address(3, "Torenstraat", 82, null, "7620CX", "Best", 2, 2);
        int searchId = expectedAddress.getId();
        
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        Optional<Address> optionalAddress = addressDao.findAddressById(searchId);
        
        // Assert we found the address and it is the address we expected
        assertTrue("Existing address should be present", optionalAddress.isPresent());
        assertEquals("Existing address should be the expected address", expectedAddress, optionalAddress.get());
    }
    
    /**
     * Test of findAddressById method, of class AddressDaoMongo.
     */
    @Test
    public void testFindNonExistingAddressById() {
        System.out.println("findNonExistingAddressById");
        
        // Define the address to be searched
        Address expectedAddress = new Address(30, "Torenstraat", 82, null, "7620CX", "Best", 2, 2);
        int searchId = expectedAddress.getId();
        
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        Optional<Address> optionalAddress = addressDao.findAddressById(searchId);
        
        // Assert we found the address and it is the address we expected
        assertFalse("Non Existing address should not be present", optionalAddress.isPresent());
    }

    /**
     * Test of findAddressesByCustomerId method, of class AddressDaoMongo.
     */
    @Test
    public void testFindAddressesByCustomerId() {
        System.out.println("findAddressesByCustomerId");
        
        int customerId = 2;
        // Define the addresses to be found
        Address expectedAddress1 = new Address(2,"Snelweg",56,null,"3922JL","Ee",2,1);
        Address expectedAddress2 = new Address(3,"Torenstraat",82,null,"7620CX","Best",2,2);
        Address expectedAddress3 = new Address(4,"Valkstraat",9,"e","2424DF","Goorle",2,3);
        
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
         List<Address> listAddress = addressDao.findAddressesByCustomerId(customerId);
        // Assert we found the addresses and it is the address we expected
        assertEquals("Customer should have three addresses", 3, listAddress.size());
        assertEquals("Type1 address is as expected", expectedAddress1, listAddress.get(0));
        assertEquals("Type2 address is as expected", expectedAddress2, listAddress.get(1));
        assertEquals("Type3 address is as expected", expectedAddress3, listAddress.get(2));
    }

    /**
     * Test of getAllAddressTypesAsList method, of class AddressDaoMongo.
     */
    @Test
    public void testGetAllAddressTypesAsList() {
        System.out.println("getAllAddressTypesAsList");
        //declare and get the addresslist to be tested
        List<String> expectedAddressTypes = new ArrayList<>();
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        List<String>addressTypes = addressDao.getAllAddressTypesAsList();
        
        expectedAddressTypes.add("postadres");
        expectedAddressTypes.add("factuuradres");
        expectedAddressTypes.add("bezorgadres");      
        
        // Assert we found the addressList and it is the addressList we expected
        assertEquals("All AddressTypes should be as expected", expectedAddressTypes, addressTypes);
    }

    /**
     * Test of getAllAddressesAsList method, of class AddressDaoMongo.
     */
    @Test
    public void testGetAllAddressesAsList() {
        System.out.println("getAllAddressesAsList");
        //declare and get the addresslist to be tested
        List<Address> expectedAddresss = new ArrayList<>();
        List<Address> addressList;
        AddressDao addressDao = DaoFactory.getDaoFactory().createAddressDao();
        addressList = addressDao.getAllAddressesAsList();
        
        expectedAddresss.add(new Address(1,"Postweg",201,"h","3781JK","Aalst",1,1));
        expectedAddresss.add(new Address(2,"Snelweg",56,null,"3922JL","Ee",2,1));
        expectedAddresss.add(new Address(3,"Torenstraat",82,null,"7620CX","Best",2,2));
        expectedAddresss.add(new Address(4,"Valkstraat",9,"e","2424DF","Goorle",2,3));
        expectedAddresss.add(new Address(5,"Dorpsstraat",5,null,"9090NM","Best",3,1));
        expectedAddresss.add(new Address(6,"Plein",45,null,"2522BH","Oss",4,1));
        expectedAddresss.add(new Address(7,"Maduralaan",23,null,"8967HJ","Apeldoorn",5,1));
        
        // Assert we found the addressList and it is the addressList we expected
        assertEquals("All Addresss should be as expected", expectedAddresss, addressList);
    }
    
}
