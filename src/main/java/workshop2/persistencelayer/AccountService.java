/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.Optional;
import workshop2.domain.Account;

/**
 *
 * @author hwkei
 */
public interface AccountService extends GenericService {

    public void updateAccount(Account account);

    public Optional<Account> findAccountByUserName(String userName);

    public void deleteAccount(Account account);

    public void createAccount(Account account);
    
}
