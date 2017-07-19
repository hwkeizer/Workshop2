/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1;

import workshop1.interfacelayer.DatabaseConnection;
import workshop1.interfacelayer.controller.FrontEndController;
import workshop1.interfacelayer.dao.DaoFactory;

/**
 *
 * @author hwkei
 */
public class Applikaasie {
    
    public static void main(String[] args) {        
        DaoFactory.setDatabaseType(0);
        FrontEndController frontEndController = new FrontEndController();
        frontEndController.login();
    }
}
