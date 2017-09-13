/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import workshop2.domain.Address;
import workshop2.domain.Customer;
import workshop2.persistencelayer.AddressService;
import workshop2.persistencelayer.AddressServiceFactory;
import workshop2.interfacelayer.view.AddressView;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */
@Component
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    @Autowired
    private AddressView addressView;
    private Address address;
     @Autowired
    private AddressService addressService;// = AddressServiceFactory.getAddressService();

    //private final CustomerService customerService = CustomerServiceFactory.getCustomerService();
   // public AddressController(AddressView addressView) {

    //    this.addressView = addressView;

   // }

    public void createAddress(CustomerController customerController) {
        // We first need a valid customer to link to the new address
        addressView.showConstructAddressStartScreen();
        Long customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip creating the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        addressView.showListOfAddresses(listAddresses);
        if (listAddresses.size() == 3) {
            addressView.showNoAddressCanBeAdded();
            return;
        }
        Optional<Customer> optionalCustomer = customerController.searchCustomerById(customerId);
        Customer customer = optionalCustomer.get();
        Optional<Address> optionalAddress = addressView.constructAddress(customer, getAvailableAddressTypes());
        if (optionalAddress.isPresent()) {
            // If the address type already exists we have to abort
            for (Address address : listAddresses) {
                if (optionalAddress.get().getAddressType() == address.getAddressType()) {
                    addressView.showAddressTypeExists();
                    return;
                }
            }
            Address address = (Address)optionalAddress.get();
            addressService.createAddress(address);
        }
    }

    public void deleteAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showDeleteAddressStartScreen();
        Long customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToDelete(listAddresses);
        if (optionalAddress.isPresent()) {
            addressService.deleteAddress(optionalAddress.get());
        }
    }

    public void updateAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showUpdateAddressStartScreen();
        Long customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToUpdate(listAddresses);
        if (optionalAddress.isPresent()) {
            addressService.updateAddress(optionalAddress.get());
        }

    }

    List<Address> listAllAddressesFromCustomer(Long customerId) {

        return addressService.findAllAddressesByCustomerId(customerId);

    }

    List<String> getAvailableAddressTypes() {
        List<String> addressType = new ArrayList<>();
        addressType.add("POSTADRES");
        addressType.add("FACTUURADRES");
        addressType.add("BEZORGADRES");

        return addressType;
    }

}
