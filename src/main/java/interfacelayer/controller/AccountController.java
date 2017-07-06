/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.controller;

import domain.Account;
import interfacelayer.view.AccountView;
import java.util.Scanner;

/**
 *
 * @author hwkei
 */
public class AccountController {
    private final Scanner input = new Scanner(System.in);    
    private AccountView accountView;
    private Account account;
            
    public AccountController(AccountView accountView) {
        this.accountView = accountView;
    }
    
    public boolean validateAccount(String userName, String password) {
        // TODO: moet nog worden geimplementeerd
        return true;
    }
    
    public String getUserRole(String userName) {
        // TODO: moet nog worden geimplementeerd
        return "medewerker";
    }
}