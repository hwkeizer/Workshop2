/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.dao;

import domain.Product;
import interfacelayer.DuplicateProductException;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author hwkei
 */
public interface ProductDao {
    
    void insertProduct(Product product) throws DuplicateProductException;
    
    void updateProduct(Product product);
    
    void deleteProduct(Product product);
    
    Optional<Product> findProductById(int productId);
    
    Optional<Product> findProductByName(String name);
}
