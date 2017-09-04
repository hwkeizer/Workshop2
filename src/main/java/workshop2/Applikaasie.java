/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;



import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.controller.FrontEndController;

/**
 *
 * @author hwkei
 */
public class Applikaasie {
    public static void main(String[] args) { 
        
        Database database = new Database();
        if (!database.init()) {
            System.out.println("Database kan niet worden gevonden, raadpleeg de applicatie beheerder");
            System.exit(1);
        }        
        FrontEndController frontEndController = new FrontEndController();
        frontEndController.login();
        // De factory moet gesloten worden om het programma af te sluiten. 
        //TODO: kijken of dit netter kan!!
        DatabaseConnection.getInstance().closeDatabase();
    }
}
