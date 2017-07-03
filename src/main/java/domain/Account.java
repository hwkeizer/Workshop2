/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author thoma
 */
public class Account {
    private int id;
    private String userName;
    private String password;
    private int accountType;
    
    public Account(){
        this.id = -1;
    }

    public Account(String userName, String password, int accountType) {
        this.id = -1;
        this.userName = userName;
        this.password = password;
        this.accountType = accountType;
    }

    public Account(int id, String userName, String password, int accountType) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.accountType = accountType;
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
    
    
}
