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
        // We first need a valid customer to link to the new address
        addressView.showConstructAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip creating the address
            return;
        }
        Optional<Address> optionalAddress = addressView.constructAddress(customerId, getAvailableAddressTypes());   
        if (optionalAddress.isPresent()) {
            addressDao.insertAddress(optionalAddress.get());
        }       
    }
    
    public void deleteAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showDeleteAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToDelete(listAddresses);
        if (optionalAddress.isPresent()) {
            addressDao.deleteAddress(optionalAddress.get());
        }            
    }
    
    public void updateAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showUpdateAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToUpdate(listAddresses);
        if (optionalAddress.isPresent()) {
            addressDao.updateAddress(optionalAddress.get());
        }            
        
    }
    
    public void searchAddress() {
        
    }
    
    List<Address> listAllAddressesFromCustomer(int customerId) {
        return addressDao.findAddressesByCustomerId(customerId);
    }
    
    List<String> getAvailableAddressTypes() {
        return addressDao.getAllAddressTypesAsList();
    }
    
}
