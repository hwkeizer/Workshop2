/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author hwkei
 */
@NamedQueries({
    @NamedQuery(
        name = "findAllOrderItemsAsListByOrder",
        query = "select i from OrderItem i where i.order = :order"
    )
})
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    
    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public OrderItem() {
        
    }
    
     // Constructor without id, id will be invalidated to a negative value
    public OrderItem(Order order, Product product, int amount, BigDecimal subTotal) {
        this.order = order;
        this.product = product;
        this.amount = amount;
        this.subTotal = subTotal;
    }
    
    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
    public String toString(){
        return "ID: " + this.id.toString() + "   amount: " + this.amount + "    subTotal: " + this.subTotal.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.order);
        hash = 83 * hash + Objects.hashCode(this.product);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
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
