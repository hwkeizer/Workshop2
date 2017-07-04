/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;


import domain.Customer;
import interfacelayer.DuplicateCustomerException;
import java.util.Optional;

/**
 *
 * @author thoma
 */
public interface CustomerDao {
    
    void insertCustomer(Customer customer) throws DuplicateCustomerException;
    
    void updateCustomer(Customer customer);
    
    void deleteCustomer(Customer customer);
    
    Optional<Customer> findCustomerById(int customerId);
    
    Optional<Customer> findCustomerByLastName(String userName);
}
