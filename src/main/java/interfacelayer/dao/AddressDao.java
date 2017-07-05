/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Address;
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
    
    Optional<Address> findAddressByCustomerId(int customerId);
}
