/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao;

import java.util.List;
import workshop2.domain.Account;
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
    
    Optional<Account> findAccountByUserName(String userName);
    
    List<String> getAllAccountTypesAsList();
    
    List<Account> getAllAccountsAsList();
    
}
