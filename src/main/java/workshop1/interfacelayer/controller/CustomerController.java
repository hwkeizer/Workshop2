/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.domain.Customer;
import workshop1.interfacelayer.dao.CustomerDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.view.CustomerView;

/**
 *
 * @author hwkei
 */
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerView customerView;
    private Customer customer;
    private final CustomerDao customerDao;
    
    public CustomerController(CustomerView customerView) {
        this.customerView = customerView;
        customerDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createCustomerDao();
    }
    
    public void createCustomer(){
        customerView.showNewCustomerScreen();
        
        String firstName = customerView.requestFirstNameInput(); 
        if (firstName == null) return; // User interupted createCustomer proces
        String lastName = customerView.requestLastNameInput();
        if (lastName == null) return; // User interupted createCustomer proces
        String prefix = customerView.requestPrefixInput();
        if (prefix == null) return;  // User interupted createCustomer proces
        
        // Prepare the customer with the validated values and add to database
        // The accountID is set to null initially, this must be added later
        customer = new Customer(firstName, lastName, prefix, null);
        customerView.showCustomerToBeCreated(customer);
        Integer confirmed = customerView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        customerDao.insertCustomer(customer);
    }
    
    public void deleteCustomer() {
        List<Customer> customerList = listAllCustomers();
        int customerListSize = customerList.size();
        Integer index = customerView.requestCustomerIdToDeleteInput(customerListSize);
        if (index == null) return;
        
        customer = customerList.get(index);
        //Promp for confirmation if this is indeed the customer to delete
        customerView.showCustomerToBeDeleted(customer);
        Integer confirmed = customerView.requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            customerDao.deleteCustomer(customer);
        }
    }
    
    public void updateCustomer() {
        //Prompt for which customer to update
        List<Customer> customerList = listAllCustomers();
        int customerListSize = customerList.size();
        
        Integer index = customerView.requestCustomerIdToUpdateInput(customerListSize);
        if (index == null) return;
        
        Customer customerBeforeUpdate = customerList.get(index);
        
        customerView.showCustomerToBeUpdated(customerBeforeUpdate);
        
        int Id = customerBeforeUpdate.getId();
        String newFirstName = customerView.requestNewFirstNameInput(); 
        if (newFirstName == null) {
            newFirstName = customerBeforeUpdate.getFirstName();
        } 
        String newLastName = customerView.requestNewLastNameInput();
        if (newLastName == null){
            newLastName = customerBeforeUpdate.getLastName();
        } 
        String newPrefix = customerView.requestNewPrefixInput();
        if (newPrefix == null){
            newPrefix = customerBeforeUpdate.getLastNamePrefix();
        }
        // The relation between customer and account will not change with this update procedure
        
        Customer customerAfterUpdate = new Customer(Id, newFirstName, newLastName, newPrefix, 
                customerBeforeUpdate.getAccountId());
        
        //Promp for confirmation of the selected update
        customerView.showCustomerUpdateChanges(customerBeforeUpdate, customerAfterUpdate);
        Integer confirmed = customerView.requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            customerDao.updateCustomer(customerAfterUpdate);
        }
    }
    
    public void searchCustomer() {
        
    }
    
    List<Customer> listAllCustomers() {
        List<Customer> customerList;
        customerList = customerDao.getAllCustomersAsList();        
        customerView.showListOfAllCustomers(customerList);        
        return customerList;
    }
    
    Integer selectCustomerId() {
         //Prompt for which customer to update
        List<Customer> customerList = listAllCustomers();
        int customerListSize = customerList.size();
        
        Integer index = customerView.requestCustomerId(customerListSize);
        if (index == null) return null;
        Customer selectedCustomer = customerList.get(index);
        return selectedCustomer.getId();
    }
}
