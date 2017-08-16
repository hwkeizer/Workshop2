/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao;

import java.util.List;
import workshop2.domain.Address;
import java.util.Optional;

/**
 *
 * @author thoma
 */
public interface AddressDao {
    
    void insertAddress(Address address);
    
    void updateAddress(Address address);
    
    void deleteAddress(Address address);
    
    Optional<Address> findAddressById(int AddressId);
    
    List<Address> findAddressesByCustomerId(int customerId);
    
    List<String> getAllAddressTypesAsList();
    
    List<Address> getAllAddressesAsList();
}
