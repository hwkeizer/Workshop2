/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.Optional;
import workshop2.domain.Customer;

/**
 *
 * @author hwkei
 */
public interface CustomerService extends GenericService {
    
    public void updateCustomer(Customer customer);

    public Optional<Customer> findCustomerByLastName(String lastName);

    public void deleteCustomer(Customer customer);

    public void createCustomer(Customer customer);
}
