/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.FrontEndController;
//import workshop2.javaconfig.PersistenceConfig;
import workshop2.javaconfig.ConfiguratieFile;
/**
 *
 * @author hwkei
 */
public class Applikaasie {
    
    public static void main(String[] args) { 
        
       // ApplicationContext context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfiguratieFile.class);
        
        FrontEndController frontEndController = context.getBean(FrontEndController.class);
        Database database = new Database();
        if (!database.init()) {
            System.out.println("Database kan niet worden gevonden, raadpleeg de applicatie beheerder");
            System.exit(1);
        }        
        //FrontEndController frontEndController = new FrontEndController();
        frontEndController.login();
        // De factory moet gesloten worden om het programma af te sluiten. 
        //TODO: kijken of dit netter kan!!
        DatabaseConnection.getInstance().closeDatabase();
    }
}
