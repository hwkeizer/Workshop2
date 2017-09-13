/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.hibernate.OrderServiceHibernate;

/**
 *
 * @author thoma
 */
@Component
public class OrderServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceFactory.class);
    private static String persistenceProvider;
    
    @Bean
    public static OrderService getOrderService(){
        persistenceProvider = DatabaseConnection.getInstance().getPersistenceProvider();
        
        switch(persistenceProvider){
            case("hibernate") :
                return new OrderServiceHibernate();
            default : {
                log.error("Geen persistentietoeleveraar gevonden. Neem contact op met de helpdesk!");
                return null;
            } 
        }
    }
}
