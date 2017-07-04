/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author hwkei
 */
public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int amount;
    private BigDecimal subTotal;
    
    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public OrderItem() {
        this.id = -1;
    }
    
     // Constructor without id, id will be invalidated to a negative value
    public OrderItem(int orderId, int productId, int amount, BigDecimal subTotal) {
        this.id = -1;
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
        this.subTotal = subTotal;
    }
    
    // Constructor with all member fields
    public OrderItem(int id, int orderId, int productId, int amount, BigDecimal subTotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        hash = 83 * hash + this.orderId;
        hash = 83 * hash + this.productId;
        hash = 83 * hash + this.amount;
        hash = 83 * hash + Objects.hashCode(this.subTotal);
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
        final OrderItem other = (OrderItem) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.orderId != other.orderId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        if (this.amount != other.amount) {
            return false;
        }
        if (!Objects.equals(this.subTotal, other.subTotal)) {
            return false;
        }
        return true;
    }
    
    
}
