/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author hwkei
 */
@NamedQueries({
    @NamedQuery(
        name = "findAllOrdersAsListByCustomer",
        query = "select i from Order i where i.customer = :customer"
    )
})
@Entity
@Table(name = "`order`")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ORDER_STATUS")
    private OrderStatus orderStatus;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public Order() {
        
    }
    
    // Constructor without id, id will be invalidated to a negative value
    public Order(BigDecimal totalPrice, Customer customer, LocalDateTime dateTime, OrderStatus orderStatus) {
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    
    
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    @Override
    public String toString() {
        return String.format("%-5d%-10.2f%-15s%-20s", this.getId(), this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatus().toString());
    }

    public String toStringNoId() {
        return String.format("%-10.2f%-15s%-20s", this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatus().toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.totalPrice);
        hash = 53 * hash + Objects.hashCode(this.customer);
        hash = 53 * hash + Objects.hashCode(this.dateTime.getYear());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getMonthValue());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getDayOfMonth());
        hash = 53 * hash + Objects.hashCode(this.orderStatus);
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
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.orderStatus, other.orderStatus)) {
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
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.orderStatus, other.orderStatus)) {
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
