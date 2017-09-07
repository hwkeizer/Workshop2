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
import workshop2.interfacelayer.view.CustomerView;
import workshop2.interfacelayer.view.Validator;
import workshop2.persistencelayer.CustomerService;
import workshop2.persistencelayer.CustomerServiceFactory;

/**
 *
 * @author hwkei
 */
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerView customerView;
    private Customer customer;
    private Optional<Customer> optionalCustomer;
    private final CustomerService customerService = CustomerServiceFactory.getCustomerService();
    
    public CustomerController(CustomerView customerView) {
        this.customerView = customerView;
    }
    
    public void createCustomer(){        
        optionalCustomer = customerView.constructCustomer();
        if (optionalCustomer.isPresent()) {
            customerService.createCustomer(optionalCustomer.get());
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
        optionalCustomer = selectCustomerByUser();
        if (!optionalCustomer.isPresent()) return; // No customer selected so abort
        // Check if Customer has already an account
        if (optionalCustomer.get().getAccount() != null) {
            Optional<Account> optionalAccount = customerService.fetchById(Account.class, 
                    optionalCustomer.get().getAccount().getId());
            if (optionalAccount.isPresent()) {
                customerView.showCustomerHasAlreadyAccount();
                return;
            }
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
        optionalCustomer.get().setAccount(optionalAccount.get());
        log.debug("Linking customer {} to account {}", optionalCustomer.get().getLastName(), optionalAccount.get().getUsername());
        customerService.updateCustomer(optionalCustomer.get());
    }
    
    public void deleteCustomer() {
        optionalCustomer = customerView.selectCustomerToDelete(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerService.deleteCustomer(optionalCustomer.get());
        }        
    }
    
    public void updateCustomer() {
        optionalCustomer = customerView.selectCustomerToUpdate(listAllCustomers());
        if (optionalCustomer.isPresent()) {
            customerService.updateCustomer(optionalCustomer.get());
        }      
    }
    
    public Optional<Customer> searchCustomerByAccount(Long accountId) {
        List<Customer> customerList = listAllCustomers();
        for (Customer cust : customerList) {
            if (cust.getAccount() != null) {
                if (Validator.isValidId(cust.getAccount().getId())) {
                    if (cust.getAccount().getId().equals(accountId)) {
                        return Optional.ofNullable(cust);
                    }
                }
            }
        }
        return Optional.empty();
    }
    
    List<Customer> listAllCustomers() {
        List<Customer> customerList;
        customerList = customerService.fetchAllAsList(Customer.class);               
        return customerList;
    }
    
    Long selectCustomerIdByUser() {
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
