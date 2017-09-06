/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import workshop2.domain.Product;
import workshop2.interfacelayer.dao.DuplicateProductException;
import workshop2.interfacelayer.view.ProductView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.persistencelayer.ProductService;
import workshop2.interfacelayer.persistencelayer.ProductServiceFactory;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductView productView;
    private Product product;
    private final ProductService productService = ProductServiceFactory.getAddressService();

    public ProductController(ProductView productView) {
        this.productView = productView;

    }

    public void createProduct() {
        productView.showNewProductScreen();

        // Prepare the product with the validated values and add it to the database
        product = productView.constructNewProduct();
        if (product == null) {
            return;
        }
        productView.showProductToBeCreated(product);
        Integer confirmed = productView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2) {
            return;
        } else {
            productService.createProduct(product);
        }
    }

    public void deleteProduct() {
        //Prompt for which product to delete
        List<Product> productList = listAllProducts();
        int productListSize = productList.size();

        Integer index = productView.requestProductIdToDeleteInput(productListSize);
        if (index == null) {
            return;
        }

        product = productList.get(index);

        //Promp for confirmation if this is indeed the product to delete
        productView.showProductToBeDeleted(product);
        Integer confirmed = productView.requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2) {
            return;
        } else {
            productService.deleteProduct(product);
        }
    }

    public void updateProduct() {
        //Prompt for which product to update
        List<Product> productList = listAllProducts();
        int productListSize = productList.size();

        Integer index = productView.requestProductIdToUpdateInput(productListSize);
        if (index == null) {
            return;
        }

        Product productBeforeUpdate = productList.get(index);

        productView.showProductToBeUpdated(productBeforeUpdate);

        Product productAfterUpdate = productView.constructUpdateProduct(productBeforeUpdate);

        //Promp for confirmation of the selected update
        productView.showProductUpdateChanges(productBeforeUpdate, productAfterUpdate);
        Integer confirmed = productView.requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2) {
            return;
        } else {
            productService.updateProduct(productAfterUpdate);
        }
    }

    public void searchProduct() {

    }

    //returntype the list of products, required for verification of which product to delete
    public List<Product> listAllProducts() {
        List<Product> productList;

        productList = productService.getAllProductsAsList();

        productView.showListOfAllProducts(productList);

        return productList;
    }
}
