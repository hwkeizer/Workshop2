/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author thoma
 */
@NamedQueries({
    @NamedQuery(
        name = "findCustomerByLastName",
        query = "select i from Customer i where i.lastName = :lastName"
    )
})
@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "LAST_NAME_PREFIX")
    private String lastNamePrefix;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
    
    public Customer() {}

    public Customer(String firstName, String lastName, String lastNamePrefix, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastNamePrefix = lastNamePrefix;
        this.account = account;
    }
    
    public Long getId() {
        return id;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.firstName);
        hash = 89 * hash + Objects.hashCode(this.lastName);
        hash = 89 * hash + Objects.hashCode(this.lastNamePrefix);
        hash = 89 * hash + Objects.hashCode(this.account);
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
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.lastNamePrefix, other.lastNamePrefix)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        return true;
    }    
        
}
