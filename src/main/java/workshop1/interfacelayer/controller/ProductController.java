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
import java.util.ArrayList;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    
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
    
    public void deleteProduct() {
        //Prompt for which product to delete
        ArrayList<Product> productList = listAllProducts();
        int productListSize = productList.size();
        log.debug("productListSize is " + productListSize);
        Integer index = productView.requestProductIdInput(productListSize);
        if (index == null) return;
        int id = productList.get(index).getId();
        
        //Retreive the product to delete from the database
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        Optional<Product> optionalProduct = productDao.findProductById(id);
        Product product = optionalProduct.get();
        
        //Promp for confirmation if this is indeed the product to delete
        productView.showProductToBeDeleted(product);
        Integer confirmed = productView.requestConfirmationToDelete(product);
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            productDao.deleteProduct(product);
        }
    }
    
    //returntype the list of products, required for verification of which product to delete
    public ArrayList<Product> listAllProducts() {
        ArrayList<Product> productList;
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        productList = productDao.getAllProductsAsList();
        
        productView.showListOfAllProducts(productList);
        
        return productList;
    }
    
    public void updateProduct() {
    
    }
    
    public void searchProduct() {
    
    }
}