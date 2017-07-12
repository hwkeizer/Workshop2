/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import java.util.List;
import java.util.Optional;
import workshop1.domain.Account;
import workshop1.interfacelayer.view.AccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop1.interfacelayer.dao.AccountDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.DuplicateAccountException;

/**
 *
 * @author hwkei
 */
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);   
    private final AccountView accountView;
    private Account account;
            
    public AccountController(AccountView accountView) {
        this.accountView = accountView;
    }
    
    public void createAccount() {
        accountView.showNewAccountScreen();
        
        String name = accountView.requestUsernameInput();
        if (name == null) return; // User interupted createAccount proces
        String password = accountView.requestPasswordInput();
        if (password == null) return; // User interupted createAccount proces
        Integer accountType = accountView.requestAccountType(getAvailableAccountTypes());
        if (accountType == null) return;  // User interupted createAccount proces
        
        // Prepare the account with the validated values and add it to the database
        account = new Account(name, password, accountType);
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        try {
            accountDao.insertAccount(account);
        } catch(DuplicateAccountException e) {
            accountView.showDuplicateAccountError();
        }
    }
    
    public void changePassword() {}
    
    public void deleteAccount() {
        //Prompt for which account to delete
        List<Account> accountList = listAllAccounts();
        int accountListSize = accountList.size();
        log.debug("accountListSize is " + accountListSize);
        Integer index = accountView.requestAccountIdInput(accountListSize);
        if (index == null) return;
        int id = accountList.get(index).getId();
        
        //Retreive the account to delete from the database
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountById(id);
        if (optionalAccount.isPresent()) account = optionalAccount.get();
        
        //Promp for confirmation if this is indeed the account to delete
        accountView.showAccountToBeDeleted(account);
        Integer confirmed = accountView.requestConfirmationToDelete(account);
        if (confirmed == null || confirmed == 2){
        }
        else {
            accountDao.deleteAccount(account);
        }
    }
        
    public boolean validateAccount(String userName, String password) {
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUsername(userName);
        if (!optionalAccount.isPresent()) return false;
        return optionalAccount.get().getPassword().equals(password);
    }
    
    public Integer getUserRole(String userName) {
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        Optional<Account> optionalAccount = accountDao.findAccountByUsername(userName);
        if (optionalAccount.isPresent()) account = optionalAccount.get();
        return account.getAccountTypeId();
    }
    
    public List<String> getAvailableAccountTypes() {
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        return accountDao.getAllAccountTypesAsList();
    }
    
    public List<Account> listAllAccounts() {
        List<Account> accountList;
        AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createAccountDao();
        accountList = accountDao.getAllAccountsAsList();        
        accountView.showListOfAllAccounts(accountList);        
        return accountList;
    }
}