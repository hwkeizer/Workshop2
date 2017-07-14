/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.Address;
import workshop1.domain.Customer;
import workshop1.interfacelayer.dao.AddressDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.view.AddressView;

/**
 *
 * @author hwkei
 */
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressView addressView;
    private Address address;
    private final AddressDao addressDao;
    
    public AddressController(AddressView addressView) {
        this.addressView = addressView;
        addressDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAddressDao();
    }
    
    public void createAddress(CustomerController customerController) {        
        // We first need a valid user to link to the new address
        addressView.showNewAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            
            return;
        }
        Optional<Address> optionalAddress = addressView.constructAddress(customerId, getAvailableAddressTypes());   
        if (optionalAddress.isPresent()) {
            addressDao.insertAddress(optionalAddress.get());
        }       
    }
    
    public void deleteAddress() {
        
    }
    
    public void updateAddress() {
        
    }
    
    public void searchAddress() {
        
    }
    
    List<Address> listAllAddresses() {
        
        return null; // dummy
    }
    
    List<String> getAvailableAddressTypes() {
        return addressDao.getAllAddressTypesAsList();
    }
    
}
