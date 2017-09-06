/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.persistencelayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.persistencelayer.hibernate.ProductServiceHibernate;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public class ProductServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceFactory.class);

    private static String persistenceProvider;

    public static ProductService getAddressService() {

        persistenceProvider = DatabaseConnection.getInstance().getPersistenceProvider();

        switch (persistenceProvider) {

            case "hibernate":

                return new ProductServiceHibernate();
                

            default: {

                log.error("Geen persistentie toeleveraar gevonden. Neem contact op met de helpdesk!");

                return null;

            }

        }

    }
    
}
