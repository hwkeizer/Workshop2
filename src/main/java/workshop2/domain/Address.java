/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */

@Entity
@Table(name = "ADDRESS")
public class Address {

    public enum AddressType {
        POSTADRES, FACTUURADRES, BEZORGADRES
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "STREETNAME")
    private String streetName;
    @Column(name = "NUMBER")
    private int number;
    @Column(name = "ADDITION")
    private String addition;
    @Column(name = "POSTALCODE")
    private String postalCode;
    @Column(name = "CITY")
    private String city;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    @Enumerated(EnumType.ORDINAL)
    private AddressType addressType;

    public Address() {

    }

    public Address(String streetName, int number, String addition, String postalCode,
            String city, Customer customer, AddressType addressType) {
        this.streetName = streetName;
        this.number = number;
        this.addition = addition;
        this.postalCode = postalCode;
        this.city = city;
        this.customer = customer;
        this.addressType = addressType;
    }

    public Address(Long id, String streetName, Integer number, String addition,
            String postalCode, String city, Customer customer, AddressType addressType) {
        this.id = id;
        this.streetName = streetName;
        this.number = number;
        this.addition = addition;
        this.postalCode = postalCode;
        this.city = city;
        this.customer = customer;
        this.addressType = addressType;
    }

    public Long getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return String.format("%-5d%-30s%-8d%-12s%-10s%-20s%-15s", getId(), getStreetName(),
                getNumber(), getAddition(), getPostalCode(), getCity(), this.getAddressType().toString());
    }

    public String toStringNoId() {
        return String.format("%-30s%-8d%-12s%-10s%-20s%-15s", getStreetName(),
                getNumber(), getAddition(), getPostalCode(), getCity(), this.getAddressType().toString());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.streetName);
        hash = 73 * hash + this.number;
        hash = 73 * hash + Objects.hashCode(this.addition);
        hash = 73 * hash + Objects.hashCode(this.postalCode);
        hash = 73 * hash + Objects.hashCode(this.city);
        hash = 73 * hash + Objects.hashCode(this.addressType);
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
        final Address other = (Address) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.addressType != other.addressType) {
            return false;
        }
        if (this.number != other.number) {
            return false;
        }
        if (!Objects.equals(this.streetName, other.streetName)) {
            return false;
        }
        if (!Objects.equals(this.addition, other.addition)) {
            return false;
        }
        if (!Objects.equals(this.postalCode, other.postalCode)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }

        return true;
    }

}
