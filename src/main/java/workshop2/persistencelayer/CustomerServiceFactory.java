/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.hibernate.CustomerServiceHibernate;

/**
 *
 * @author hwkei
 */
public class CustomerServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceFactory.class);
    private static String persistenceProvider;
    
    public static CustomerService getCustomerService(){
        persistenceProvider = DatabaseConnection.getInstance().getPersistenceProvider();
        
        switch(persistenceProvider){
            case "hibernate" :
                return new CustomerServiceHibernate();
            default : {
                log.error("Geen persistentie toeleveraar gevonden. Neem contact op met de helpdesk!");
                return null;
            } 
        }
    }
}
