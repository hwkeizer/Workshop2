/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author hwkei
 */
public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int stock;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public Product() { 
        this.id = -1;
    }
    
    // Constructor without id, id will be invalidated to a negative value
    public Product(String name, BigDecimal price, int stock) {
        this(-1, name, price, stock);
    }
    
    // Constructor with all member fields
    public Product(int id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString(){
        return String.format("%-5d%-30s%10.2f%10d", this.getId(), this.getName(), this.getPrice(), this.getStock());
    }
    
    public String toStringNoId(){
        return String.format("%-30s%10.2f%10d", this.getName(), this.getPrice(), this.getStock());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + Objects.hashCode(this.price);
        hash = 73 * hash + this.stock;
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
        final Product other = (Product) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.stock != other.stock) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return true;
    }

    
    
}