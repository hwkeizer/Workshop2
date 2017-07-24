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
import workshop1.interfacelayer.DatabaseConnection;
import workshop1.interfacelayer.dao.AccountDao;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.DuplicateAccountException;
import workshop1.interfacelayer.view.Validator;

/**
 *
 * @author hwkei
 */
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);   
    private final AccountView accountView;
    private Account account;
    private AccountDao accountDao;
    
    // Public constructor only requires accountView parameter
    public AccountController(AccountView accountView) {
        this.accountView = accountView;
        accountDao = DaoFactory.getDaoFactory().createAccountDao();
    }
    
    // Package private constructor can be injected with accountView AND AccountDao for 
    AccountController(AccountView accountView, AccountDao accountDao) {
        this.accountView = accountView;
        this.accountDao = accountDao;
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
        try {
            accountDao.insertAccount(account);
        } catch(DuplicateAccountException e) {
            accountView.showDuplicateAccountError();
        }
    }
       
    public void updateAccount() {
        // Prompt for which account to update
        List<Account> accountList = listAllAccounts();
        int accountListSize = accountList.size();
        
        Integer index = accountView.requestAccountIdToUpdateInput(accountListSize);
        if (index == null) return;
        
        Account accountBeforeUpdate = accountList.get(index);
        accountView.showAccountToBeUpdated(accountBeforeUpdate);
        int Id = accountBeforeUpdate.getId();
        String newName = accountView.requestUpdateUsernameInput();
        if (newName == null) {
            newName = accountBeforeUpdate.getUsername();
        }
        String newPassword = accountView.requestUpdatePasswordInput();
        if (newPassword == null) {
            newPassword = accountBeforeUpdate.getPassword();
        }
        Integer newAccountType = accountView.requestUpdateAccountType(getAvailableAccountTypes());
        if (newAccountType == null) {
            newAccountType = accountBeforeUpdate.getAccountTypeId();
        }
        
        Account accountAfterUpdate = new Account(Id, newName, newPassword, newAccountType);
        //Promp for confirmation of the selected update
        accountView.showAccountUpdateChanges(accountBeforeUpdate, accountAfterUpdate);
        Integer confirmed = accountView.requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            log.debug(accountAfterUpdate.toString());
            accountDao.updateAccount(accountAfterUpdate);
        }
    }
    
    public void changeOwnPassword(String userName) {
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(userName);
        if (!optionalAccount.isPresent()) {
            log.error("Gebruiker {} niet gevonden in de database!", userName);
            return;
        }
        account = optionalAccount.get();
        String oldPassword = accountView.requestOldPasswordInput();
        if (validateAccount(userName, oldPassword)) {
            String newPassword = accountView.requestNewPasswordInput();
            if (newPassword != null && Validator.isValidPassword(newPassword)) {
                account.setPassword(newPassword);
                accountDao.updateAccount(account);
            }
        } else {
            accountView.showInvalidOldPassword();
        }
    }
    
    public void deleteAccount() {
        //Prompt for which account to delete
        List<Account> accountList = listAllAccounts();
        int accountListSize = accountList.size();
        log.debug("accountListSize is " + accountListSize);
        Integer index = accountView.requestAccountIdInput(accountListSize);
        if (index == null) return;
        int id = accountList.get(index).getId();
        
        //Retreive the account to delete from the database
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
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(userName);
        if (!optionalAccount.isPresent()) return false;
        return optionalAccount.get().getPassword().equals(password);
    }
    
    public Integer getUserRole(String userName) {
        Optional<Account> optionalAccount = accountDao.findAccountByUserName(userName);
        if (optionalAccount.isPresent()) account = optionalAccount.get();
        return account.getAccountTypeId();
    }
    
    public List<String> getAvailableAccountTypes() {
        return accountDao.getAllAccountTypesAsList();
    }
    
    public List<Account> listAllAccounts() {
        List<Account> accountList;
        accountList = accountDao.getAllAccountsAsList();        
        accountView.showListOfAllAccounts(accountList);        
        return accountList;
    }
    
    public Optional<Account> selectAccountByUser() {
        return accountView.selectAccount(listAllAccounts());
    }
    
    /**
     * Sets a new datasetype to be used. This option is only used for demonstration
     * purposes as this will also replace the database that is being used
     * @return 
     */
    public boolean setDatabaseType() {
        Integer newType = accountView.requestDatabaseTypeInput(DatabaseConnection.getInstance().getDatabaseType());
        if (newType == null) return false; // nothing changed
        if (newType == 1) {
            DatabaseConnection.getInstance().setDatabaseType("MYSQL");
        } else {
            DatabaseConnection.getInstance().setDatabaseType("MONGO");
        }
        // Databasetype has been changed
        return true;
    }
    
    /**
     * Sets the connection pool on or off. This option is only used for demonstration
     * purposes
     */
    public void setConnectionPool() {
        boolean newPoolSetting = accountView.requestConnectionPoolInput(DatabaseConnection.getInstance().getUseConnectionPool());
        System.out.println(newPoolSetting);
        DatabaseConnection.getInstance().useConnectionPool(newPoolSetting);
    }
    
    public void showCurrentDatabaseSettings() {
        
    }
}