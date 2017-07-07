/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao;


import workshop1.domain.Customer;
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
