/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.domain.Customer;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.view.CustomerView;
import workshop2.interfacelayer.view.Validator;

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
        
        // Select the customer
        Optional<Customer> optionalCustomer = selectCustomerByUser();
        if (!optionalCustomer.isPresent()) return; // No customer selected so abort
        if (Validator.isValidId(optionalCustomer.get().getAccountId())) {
            customerView.showCustomerHasAlreadyAccount();
            return;
        }
        
        // Select the account
        Optional<Account> optionalAccount = accountController.selectAccountByUser();
        if (!optionalAccount.isPresent()) return; // No account selected so abort
        Optional<Customer> cust = searchCustomerByAccount(optionalAccount.get().getId());
        if (cust.isPresent()) {
            customerView.showAccountIsAlreadyInUse(cust.get());
            return;
        }
        
        // if all is ok link customer and account
        optionalCustomer.get().setAccountId(optionalAccount.get().getId());
        log.debug("Linking customer {} to account {}", optionalCustomer.get().getLastName(), optionalAccount.get().getUsername());
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
    
    public Optional<Customer> searchCustomerByAccount(Integer accountId) {
        List<Customer> customerList = listAllCustomers();
        for (Customer cust : customerList) {
            if (Validator.isValidId(cust.getAccountId())) {
                if (cust.getAccountId().equals(accountId)) {
                    return Optional.ofNullable(cust);
                }
            }            
        }
        return Optional.empty();
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
