/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.domain.Address;
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
import workshop2.interfacelayer.dao.AddressDao;

/**
 *
 * @author thoma
 */
public class AddressDaoMysql implements AddressDao {
 
    private static final Logger log = LoggerFactory.getLogger(AccountDaoMysql.class);
    
    private static final String SQL_INSERT = "INSERT INTO address (street_name, number, addition, postal_code, city, customer_id, address_type_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_CUSTOMER_ID = "SELECT * FROM address WHERE `customer_id` = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM address WHERE id = ?";
    private static final String SQL_LIST_ALL_ADDRESSTYPES = "SELECT `type` FROM `address_type`";
    private static final String SQL_UPDATE = "UPDATE address SET street_name=?, number=?, addition=?, postal_code=?, city=?, customer_id=?, address_type_id=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM address WHERE id = ?";
    public AddressDaoMysql() {
    }

    @Override
    public void insertAddress(Address address) {
        log.debug("insertMethod was called");
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);) {         
            
            statement.setString(1, address.getStreetName());
            statement.setInt(2, address.getNumber());
            statement.setString(3, address.getAddition());
            statement.setString(4, address.getPostalCode());
            statement.setString(5, address.getCity());
            statement.setInt(6, address.getCustomerId());
            statement.setInt(7, address.getAddressTypeId());
            
            log.debug("Statements are set");
            
            // Execute the prepared query and validate the affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Onbekende fout, het toevoegen van address voor customerId {} is mislukt!",
                        address.getCustomerId());
            } else {
                log.debug("Address toegevoegd: {} {}", address.getCustomerId());
            }
        } 
        catch (SQLException ex) {
            // If we find errorCode 1062 (duplicate value) we throw that, else we have an unknown ConstraintException and we log that for debugging
                log.error("SQL error: ", ex);
        }
    }

    @Override
    public void updateAddress(Address address) {
        // Do nothing if the address cannot be found in the database
        if ((findAddressById(address.getId())) == null) {
            log.error("AddressId '{}' bestaat niet in de database en kan dus "
                    + "ook niet worden bijgewerkt!", address.getId());
            return;
        }
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);) {
            
            statement.setString(1, address.getStreetName());
            statement.setInt(2, address.getNumber());
            statement.setString(3, address.getAddition());
            statement.setString(4, address.getPostalCode());
            statement.setString(5, address.getCity());
            statement.setInt(6, address.getCustomerId());
            statement.setInt(7, address.getAddressTypeId());
            statement.setInt(8, address.getId());
                 
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het aanpassen van address {}{} is helaas mislukt!", 
                        address.getStreetName(), address.getNumber());
            }        
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }        
    }

    @Override
    public void deleteAddress(Address address) {
        
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);) {
            
            statement.setInt(1, address.getId());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Het verwijderen van address {}{} is helaas mislukt!", 
                        address.getStreetName(), address.getNumber());
            }
            else {
                log.debug("Adres {}{} is verwijderd uit de database", 
                        address.getStreetName(), address.getNumber());
            }
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
    }

    @Override
    public Optional<Address> findAddressById(int addressId) {
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);){
            
            statement.setInt(1, addressId);
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

    @Override
    public List<Address> findAddressesByCustomerId(int customerId) {
        List<Address> addressList = new ArrayList<>();
        try (
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CUSTOMER_ID);){
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                addressList.add(map(resultSet));
            }            
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return addressList;
    }
    
        @Override
    public List<String> getAllAddressTypesAsList() {
        List<String> allAddressTypes = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getMySqlConnection();
            Statement statement = connection.createStatement();            
            ResultSet resultSet = statement.executeQuery(SQL_LIST_ALL_ADDRESSTYPES);            
            while (resultSet.next()) {
                allAddressTypes.add(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            log.error("SQL error: ", ex);
        }
        return allAddressTypes;
    }

    @Override
    public List<Address> getAllAddressesAsList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Address map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String streetName = resultSet.getString("street_name");
        int number = resultSet.getInt("number");
        String addition = resultSet.getString("addition");
        String postalCode = resultSet.getString("postal_code");
        String city = resultSet.getString("city");
        int customerId = resultSet.getInt("customer_id");
        int addressTypeId = resultSet.getInt("address_type_id");
        return new Address(id, streetName, number, addition, postalCode, city, customerId, addressTypeId);
    }    
}
