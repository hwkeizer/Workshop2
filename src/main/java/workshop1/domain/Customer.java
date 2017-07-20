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
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String lastNamePrefix;
    private Integer accountId;
    
    public Customer(){
        
    }

    public Customer(String firstName, String lastName, String lastNamePrefix, Integer accountId) {
        this.id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastNamePrefix = lastNamePrefix;
        this.accountId = accountId;
    }
    
    public Customer(int id, String firstName, String lastName, String lastNamePrefix, Integer accountId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastNamePrefix = lastNamePrefix;
        this.accountId = accountId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNamePrefix() {
        return lastNamePrefix;
    }

    public void setLastNamePrefix(String lastNamePrefix) {
        this.lastNamePrefix = lastNamePrefix;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public String toString(){
        if(getLastNamePrefix() != null) 
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), getLastNamePrefix(), getLastName());
        
        else
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), "", getLastName());
    }
    
    public String toStringNoId(){
        if(getLastNamePrefix() != null) 
            return String.format("%-20s%-15s%-20s", getFirstName(), getLastNamePrefix(), getLastName());
        else
            return String.format("%-20s%-15s%-20s", getFirstName(), "", getLastName());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.firstName);
        hash = 73 * hash + Objects.hashCode(this.lastName);
        hash = 73 * hash + Objects.hashCode(this.lastNamePrefix);
        hash = 73 * hash + this.accountId;
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
        final Customer other = (Customer) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.accountId != other.accountId) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.lastNamePrefix, other.lastNamePrefix)) {
            return false;
        }
        return true;
    }
    
}
