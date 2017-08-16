/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2;



import workshop2.interfacelayer.controller.FrontEndController;

/**
 *
 * @author hwkei
 */
public class Applikaasie {
    public static void main(String[] args) { 
        
        DatabaseInit dbInit = new DatabaseInit();
        dbInit.installDatabase();
        
        FrontEndController frontEndController = new FrontEndController();
        frontEndController.login();
    }
}
