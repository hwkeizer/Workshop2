/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Product;

/**
 *
 * @author hwkei
 */
public interface ProductDao {
    
    void insertProduct(Product product);
    
    void updateProduct(Product product);
    
    void deleteProduct(Product product);
    
    Product findProductById(int productId);
    
    Product findProductByName(String name);
}
