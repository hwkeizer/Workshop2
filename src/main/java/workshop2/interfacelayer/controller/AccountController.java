/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.util.List;
import java.util.Optional;
import workshop2.domain.Account;
import workshop2.interfacelayer.view.AccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import workshop2.domain.AccountType;
import workshop2.interfacelayer.view.Validator;
import workshop2.persistencelayer.AccountService;
import workshop2.persistencelayer.AccountServiceFactory;

/**
 *
 * @author hwkei
 */
@Component
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);   
    @Autowired
    private  AccountView accountView;
    private Account account;
    private Optional<Account> optionalAccount;   
    @Autowired
    private AccountService accountService;// = AccountServiceFactory.getAccountService();

    
    // Public constructor only requires accountView parameter
   // public AccountController(AccountView accountView) {
     //   this.accountView = accountView;
  //  }

    
    public void createAccount() {
        
        // Collect the information from the user
        accountView.showNewAccountScreen();        
        String name = accountView.requestUsernameInput();
        if (name == null) return; // User interupted createAccount proces
        String password = accountView.requestPasswordInput();
        if (password == null) return; // User interupted createAccount proces        
        password = PasswordHash.generateHash(password); 
        AccountType accountType = accountView.requestAccountType();
        if (accountType == null) return;  // User interupted createAccount proces
        
        // Prepare the account with the validated values and add it to the database
        account = new Account(name, password, accountType);
        accountService.createAccount(account);
    }
       
    public void updateAccount() {
        // Collect the information from the user
        List<Account> accountList = listAllAccounts();
        int accountListSize = accountList.size();        
        Long index = accountView.requestAccountIdToUpdateInput(accountListSize);
        if (index == null) return;
        account = accountList.get(index.intValue());        
        accountView.showAccountToBeUpdated(account);
        String newName = accountView.requestUpdateUsernameInput();
        if (newName == null) {
            newName = account.getUsername();
        }
        String newPassword = accountView.requestUpdatePasswordInput();
        if (newPassword == null) {            
            newPassword = account.getPassword();
        } else {
            newPassword = PasswordHash.generateHash(newPassword);
        }
        AccountType newAccountType = accountView.requestUpdateAccountType();
        if (newAccountType == null) {
            newAccountType = account.getAccountType();
        }
        Account newValues = new Account(newName, newPassword, newAccountType);
        
        //Promp for confirmation of the selected update
        accountView.showAccountUpdateChanges(account, newValues);
        Integer confirmed = accountView.requestConfirmationToUpdate();
        
        // Update the account or skip the update if user cancels
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            log.debug("Changing account: \n{}\nto\n{}", account.toStringNoId(), newValues.toStringNoId());
            account.setUsername(newName);
            account.setPassword(newPassword);
            account.setAccountType(newAccountType);
            accountService.updateAccount(account);
            
        }
    }
    
    public void changeOwnPassword(String userName) {
        optionalAccount = accountService.findAccountByUserName(userName);
        if (!optionalAccount.isPresent()) {
            log.error("Gebruiker {} niet gevonden in de database!", userName);
            return;
        }
        account = optionalAccount.get();
        String oldPassword = accountView.requestOldPasswordInput();
        if (validateAccount(userName, oldPassword)) {
            String newPassword = accountView.requestNewPasswordInput();
            if (newPassword != null && Validator.isValidPassword(newPassword)) {
                // create a password hash from his password and store this in the database
                newPassword = PasswordHash.generateHash(newPassword);
                account.setPassword(newPassword);
                accountService.updateAccount(account);
            }
        } else {
            accountView.showInvalidOldPassword();
        }
    }
    
    public void deleteAccount() {
        
        // Collect the information from the user
        List<Account> accountList = listAllAccounts();
        int accountListSize = accountList.size();
        log.debug("accountListSize is " + accountListSize);
        Integer index = accountView.requestAccountIdInput(accountListSize);
        if (index == null) return;
        Long id = accountList.get(index).getId();

        //Retreive the account to delete from the database
        optionalAccount = accountService.fetchById(Account.class, id);
        if (optionalAccount.isPresent()) account = optionalAccount.get();
        
        //Promp for confirmation if this is indeed the account to delete
        accountView.showAccountToBeDeleted(account);
        Integer confirmed = accountView.requestConfirmationToDelete(account);
        if (confirmed == null || confirmed == 2){
        }
        else {
            accountService.deleteAccount(account);
        }
    }
        
    public boolean validateAccount(String userName, String password) {
        optionalAccount = accountService.findAccountByUserName(userName);
        if (!optionalAccount.isPresent()) return false;
        return PasswordHash.validatePassword(password, optionalAccount.get().getPassword());
    }
    
    public AccountType getUserRole(String userName) {
       optionalAccount = accountService.findAccountByUserName(userName);
        if (optionalAccount.isPresent()) account = optionalAccount.get();
        return account.getAccountType();
    }
    
    public List<Account> listAllAccounts() {
        List<Account> accountList; 
        accountList = accountService.<Account>fetchAllAsList(Account.class);
        accountView.showListOfAllAccounts(accountList);        
        return accountList;
    }
    
    public Optional<Account> selectAccountByUser() {
        return accountView.selectAccount(listAllAccounts());
    }
}