/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer.controller;

import domain.Product;
import interfacelayer.DuplicateProductException;
import interfacelayer.dao.DaoFactory;
import interfacelayer.dao.ProductDao;
import interfacelayer.view.ProductView;
import java.math.BigDecimal;
import util.Validator;

/**
 *
 * @author hwkei
 */
public class ProductController {
    private final ProductView productView;
    private Product product;
    
    public ProductController() {
        productView = new ProductView();
    }
    
    public void createProduct() {
                
        productView.showNewProductScreen();
        
        String name = getProductNameFromUser(); 
        if (name == null) return; // User interupted createProduct proces
        String price = getProductPriceFromUser();
        if (price == null) return; // User interupted createProduct proces
        String stock = getProductStockFromUser();
        if (stock == null) return;  // User interupted createProduct proces
        
        // Prepare the product with the validated values and add it to the database
        product = new Product(name, new BigDecimal(price), Integer.parseInt(stock));
        ProductDao productDao = DaoFactory.getDaoFactory(DaoFactory.MYSQL).createProductDao();
        try {
            productDao.insertProduct(product);
        } catch(DuplicateProductException e) {
            System.out.println("\nFout: U probeert een product toe te voegen dat al bestaat in de database.\n"
                    + "Als u het bestaande product wilt wijzigen kies dan voor 'Wijzigen product'"); 
        }        
    }
    public void deleteProduct() {}
    public void updateProduct() {}
    public void listAllProducts() {}
    public void searchProduct() {}
    
    private String getProductNameFromUser() {        
        String name = productView.showNameRequest();
        if (name.equals("!")) return null; // User interuption
        while (!Validator.isValidNameString(name)) {
            productView.showInvalidRespons();            
            name = productView.showNameRequest();
            if (name.equals("!")) return null; // User interuption
        }
        return name;
    }
    
    private String getProductPriceFromUser() {        
        String price = productView.showPriceRequest();
        if (price.equals("!")) return null; // User interuption
        while (!Validator.isValidBigDecimal(price)) {
            productView.showInvalidRespons();            
            price = productView.showPriceRequest();
            if (price.equals("!")) return null; // User interuption
        }
        return price;
    }
    
    private String getProductStockFromUser() {        
        String stock = productView.showStockRequest();
        if (stock.equals("!")) return null; // User interuption
        while (!Validator.isValidInt(stock)) {
            productView.showInvalidRespons();            
            stock = productView.showStockRequest();
            if (stock.equals("!")) return null; // User interuption
        }
        return stock;
    }       
}