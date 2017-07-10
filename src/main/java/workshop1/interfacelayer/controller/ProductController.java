/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.controller;

import workshop1.domain.Product;
import workshop1.interfacelayer.dao.DuplicateProductException;
import workshop1.interfacelayer.dao.DaoFactory;
import workshop1.interfacelayer.dao.ProductDao;
import workshop1.interfacelayer.view.ProductView;
import java.math.BigDecimal;
import workshop1.interfacelayer.view.Validator;

/**
 *
 * @author hwkei
 */
public class ProductController {
    private final ProductView productView;
    private Product product;
    
    public ProductController(ProductView productView) {
        this.productView = productView;
    }
    
    public void createProduct() {
                
        productView.showNewProductScreen();
        
        String name = productView.requestNameInput(); 
        if (name == null) return; // User interupted createProduct proces
        BigDecimal price = productView.requestPriceInput();
        if (price == null) return; // User interupted createProduct proces
        Integer stock = productView.requestStockInput();
        if (stock == null) return;  // User interupted createProduct proces

        // Prepare the product with the validated values and add it to the database
        product = new Product(name, price, stock);
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        try {
            productDao.insertProduct(product);
        } catch(DuplicateProductException e) {
            productView.showDuplicateProductError();
        }
    }
    public void deleteProduct() {}
    public void updateProduct() {}
    public void listAllProducts() {}
    public void searchProduct() {}
}