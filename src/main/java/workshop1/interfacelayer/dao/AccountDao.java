/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao;

import workshop1.domain.Account;
import workshop1.domain.Product;
import java.util.Optional;

/**
 *
 * @author thoma
 */
public interface AccountDao {
    
    void insertAccount(Account account) throws DuplicateAccountException;
    
    void updateAccount(Account account);
    
    void deleteAccount(Account account);
    
    Optional<Account> findAccountById(int AccountId);
    
    Optional<Account> findAccountByUsername(String userName);
    
}
