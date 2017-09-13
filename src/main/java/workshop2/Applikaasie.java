/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.FrontEndController;

/**
 *
 * @author hwkei
 */
@Configuration
@ComponentScan(basePackages = "workshop2")
public class Applikaasie {
    @Autowired
    FrontEndController frontEndController;
    
    public static void main(String[] args) { 
        
        ApplicationContext context = new AnnotationConfigApplicationContext(Applikaasie.class);
        
        Applikaasie app = context.getBean(Applikaasie.class);
        app.run();

    }
    
    void run(){
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
