/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author hwkei
 */
public class Order {
    private int id;
    private BigDecimal totalPrice;
    private int customerId;
    private LocalDateTime dateTime;
    private int orderStatusId;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public Order() {
        this.id = -1;
    }
    
    // Constructor without id, id will be invalidated to a negative value
    public Order(BigDecimal totalPrice, Integer customerId, LocalDateTime dateTime, Integer orderStatusId) {
        this(-1, totalPrice, customerId, dateTime, orderStatusId);
    }
    
    // Constructor with all member fields
    public Order(int id, BigDecimal totalPrice, Integer customerId, LocalDateTime dateTime, Integer orderStatusId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.dateTime = dateTime;
        this.orderStatusId = orderStatusId;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }
    
    public String getOrderStatusIdWord() {
        String orderStatusWord = "";
        switch(orderStatusId){
            case 1: {
                orderStatusWord = "nieuw";
                break;
            }
            case 2: {
                orderStatusWord = "in behandeling";
                break;
            } 
            case 3: {
                orderStatusWord = "afgehandeld";
                break;
            }
        }
        return orderStatusWord;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }
    
    @Override
    public String toString() {
        return String.format("%-5d%-10.2f%-15s%-20s", this.getId(), this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatusIdWord());
    }

    public String toStringNoId() {
        return String.format("%-10.2f%-15s%-20s", this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatusIdWord());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.totalPrice);
        hash = 53 * hash + this.customerId;
        hash = 53 * hash + Objects.hashCode(this.dateTime.getYear());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getMonthValue());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getDayOfMonth());
        hash = 53 * hash + this.orderStatusId;
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
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        if (this.orderStatusId != other.orderStatusId) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }
    
    public boolean equalsNoId(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.customerId != other.customerId) {
            return false;
        }
        if (this.orderStatusId != other.orderStatusId) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }
    
    
}
