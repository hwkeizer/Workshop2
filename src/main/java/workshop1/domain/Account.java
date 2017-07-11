/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.domain;


import java.util.Objects;

/**
 *
 * @author thoma
 */
public class Account {
    private int id;
    private String username;
    private String password;
    private int accountTypeId;
    
    public Account(){
        this.id = -1;
    }

    public Account(String username, String password, int accountTypeId) {
        this.id = -1;
        this.username = username;
        this.password = password;
        this.accountTypeId = accountTypeId;
    }

    public Account(int id, String username, String password, int accountTypeId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountTypeId = accountTypeId;
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountType(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }
    
     @Override
    public String toString(){
        return String.format("%-5d%-20s%-20s%-5d", this.getId(), this.getUsername(), this.getPassword(), this.getAccountTypeId());
    }
    
    public String toStringNoId(){
        return String.format("%-20s%-20s%-5d", this.getUsername(), this.getPassword(), this.getAccountTypeId());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.username);
        hash = 73 * hash + Objects.hashCode(this.password);
        hash = 73 * hash + this.accountTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.accountTypeId != other.accountTypeId) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }
    
    
}
