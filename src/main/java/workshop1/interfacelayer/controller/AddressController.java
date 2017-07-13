/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.util.List;
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
        
        addressView.showNewAddressStartScreen();
        Integer customerId = customerController.selectCustomerId();
        addressView.showNewAddressContinueScreen();
        
        String streetName = addressView.requestStreetNameInput(); 
        if (streetName == null) return; // User interupted createAddress proces
        Integer number = addressView.requestNumberInput();
        if (number == null) return; // User interupted createAddress proces
        String addition = addressView.requestAdditionInput();
        if (addition == null) return;  // User interupted createAddress proces
        String postalCode = addressView.requestPostalCodeInput();
        if (postalCode == null) return;  // User interupted createAddress proces
        String city = addressView.requestCityInput();
        if (city == null) return;  // User interupted createAddress proces
        Integer addressType = addressView.requestAddressType(getAvailableAddressTypes());
        if (addressType == null) return;  // User interupted createAccount proces
        
        // Prepare the address with the validated values and add to the database
        // The customerID is set to null initially, this must be added later
        address = new Address(streetName, number, addition, postalCode, city, customerId, addressType);
        addressView.showAddressToBeCreated(address);
        Integer confirmed = addressView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        addressDao.insertAddress(address);
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
