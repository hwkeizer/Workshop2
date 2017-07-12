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
import java.util.List;
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
    private ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
    
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
        productView.showProductToBeCreated(product);
        Integer confirmed = productView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            try {
                productDao.insertProduct(product);
            } catch(DuplicateProductException e) {
                productView.showDuplicateProductError();
            }
        }
    }
    
    public void deleteProduct() {
        //Prompt for which product to delete
        List<Product> productList = listAllProducts();
        int productListSize = productList.size();
        
        Integer index = productView.requestProductIdToDeleteInput(productListSize);
        if (index == null) return;
        
        product = productList.get(index);
        
        //Promp for confirmation if this is indeed the product to delete
        productView.showProductToBeDeleted(product);
        Integer confirmed = productView.requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            productDao.deleteProduct(product);
        }
    }
            
    public void updateProduct() {
        //Prompt for which product to update
        List<Product> productList = listAllProducts();
        int productListSize = productList.size();
        
        Integer index = productView.requestProductIdToUpdateInput(productListSize);
        if (index == null) return;
        
        Product productBeforeUpdate = productList.get(index);
        
        productView.showProductToBeUpdated(productBeforeUpdate);
        
        int iD = productBeforeUpdate.getId();
        String newName = productView.requestNewNameInput(); 
        if (newName == null) {
            newName = productBeforeUpdate.getName();
        } 
        BigDecimal newPrice = productView.requestNewPriceInput();
        if (newPrice == null){
            newPrice = productBeforeUpdate.getPrice();
        } 
        Integer newStock = productView.requestNewStockInput();
        if (newStock == null){
            newStock = productBeforeUpdate.getStock();
        }

        Product productAfterUpdate = new Product(iD, newName, newPrice, newStock);
        
        
        //Promp for confirmation of the selected update
        productView.showProductUpdateChanges(productBeforeUpdate, productAfterUpdate);
        Integer confirmed = productView.requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            productDao.updateProduct(productAfterUpdate);
        }
    }
    
    public void searchProduct() {
    
    }
    
    //returntype the list of products, required for verification of which product to delete
    public List<Product> listAllProducts() {
        List<Product> productList;
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        productList = productDao.getAllProductsAsList();
        
        productView.showListOfAllProducts(productList);
        
        return productList;
    }
}