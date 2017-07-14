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
        Optional<Customer> optionalCustomer = customerView.constructCustomer();
        if (optionalCustomer.isPresent()) {
            customerDao.insertCustomer(optionalCustomer.get());
        }
    }
    
    public void deleteCustomer() {
        Optional<Customer> optionalCustomer = customerView.constructCustomerToDelete(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerDao.deleteCustomer(optionalCustomer.get());
        }        
    }
    
    public void updateCustomer() {
        Optional<Customer> optionalCustomer = customerView.constructCustomerToUpdate(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerDao.updateCustomer(optionalCustomer.get());
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
    
    Integer selectCustomerIdByUser() {
        Optional<Customer> optionalCustomer = customerView.selectCustomer(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get().getId();
        }
        return null;
    }
}
