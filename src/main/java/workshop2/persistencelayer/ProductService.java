/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.List;
import workshop2.domain.Product;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public interface ProductService extends GenericService {

    public void createProduct(Product product);

    public void updateProduct(Product product);

    public void deleteProduct(Product product);

//    public List<Product> getAllProductsAsList();

}
