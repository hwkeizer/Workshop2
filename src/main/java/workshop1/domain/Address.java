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
public class Address {
    private int id;
    private String streetName;
    private int number;
    private String addition;
    private String postalCode;
    private String city;
    private Integer customerId;
    private int addressTypeId;

    public Address(String streetName, int number, String addition, String postalCode, String city, Integer customerId, int addressTypeId) {
        this.streetName = streetName;
        this.number = number;
        this.addition = addition;
        this.postalCode = postalCode;
        this.city = city;
        this.customerId = customerId;
        this.addressTypeId = addressTypeId;
    }

    public Address(int id, String streetName, int number, String addition, String postalCode, String city, int customerId, int addressTypeId) {
        this.id = id;
        this.streetName = streetName;
        this.number = number;
        this.addition = addition;
        this.postalCode = postalCode;
        this.city = city;
        this.customerId = customerId;
        this.addressTypeId = addressTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(int addressTypeId) {
        this.addressTypeId = addressTypeId;
    }
    
    @Override
    public String toString(){
        return String.format("%-5d%-30s%-8d%-12s%-10s%-30s", getId(), getStreetName(), 
                getNumber(), getAddition(), getPostalCode(), getCity());
    }
    
    public String toStringNoId(){
        return String.format("%-30s%-8d%-12s%-10s%-30s", getStreetName(), getNumber(), 
                getAddition(), getPostalCode(), getCity());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.streetName);
        hash = 73 * hash + this.number;
        hash = 73 * hash + Objects.hashCode(this.addition);
        hash = 73 * hash + Objects.hashCode(this.postalCode);
        hash = 73 * hash + Objects.hashCode(this.city);
        hash = 73 * hash + this.customerId;
        hash = 73 * hash + this.addressTypeId;
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
        if (this.addressTypeId != other.addressTypeId) {
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
