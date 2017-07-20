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
import workshop1.domain.Account;
import workshop1.domain.Customer;
import workshop1.interfacelayer.dao.CustomerDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.view.CustomerView;
import workshop1.interfacelayer.view.Validator;

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
        customerDao = DaoFactory.getDaoFactory().createCustomerDao();
    }
    
    public void createCustomer(){        
        Optional<Customer> optionalCustomer = customerView.constructCustomer();
        if (optionalCustomer.isPresent()) {
            customerDao.insertCustomer(optionalCustomer.get());
        }
    }
    
    /**
     * Link an account to a customer
     * A customer can only have one account.
     * @param accountController 
     */
    public void linkAccountToCustomer(AccountController accountController) {
        customerView.showLinkAccountToCustomerScreen();
        
        Optional<Customer> optionalCustomer = selectCustomerByUser();
        if (!optionalCustomer.isPresent()) return; // No customer selected so abort
        if (!Validator.isPositiveInteger(optionalCustomer.get().getAccountId().toString())) {
            customerView.showCustomerHasAlreadyAccount();
            return;
        }
        Optional<Account> optionalAccount = accountController.selectAccountByUser();
        if (!optionalAccount.isPresent()) return; // No account selected so abort
        // TODO: check inbouwen dat account niet al ergens aan gekoppeld is!
        optionalCustomer.get().setAccountId(optionalAccount.get().getId());
        log.debug("Linking customerid {} to accountid {}", optionalCustomer.get().getId(), optionalAccount.get().getId());
        customerDao.updateCustomer(optionalCustomer.get());
    }
    
    public void deleteCustomer() {
        Optional<Customer> optionalCustomer = customerView.selectCustomerToDelete(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerDao.deleteCustomer(optionalCustomer.get());
        }        
    }
    
    public void updateCustomer() {
        Optional<Customer> optionalCustomer = customerView.selectCustomerToUpdate(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerDao.updateCustomer(optionalCustomer.get());
        }        
    }
    
    public void searchCustomer() {        
    }
    
    List<Customer> listAllCustomers() {
        List<Customer> customerList;
        customerList = customerDao.getAllCustomersAsList();               
        return customerList;
    }
    
    Integer selectCustomerIdByUser() {
        Optional<Customer> optionalCustomer = customerView.selectCustomer(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get().getId();
        }
        return null;
    }
    
    Optional<Customer> selectCustomerByUser() {
        return customerView.selectCustomer(listAllCustomers());
    }
}
