/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workshop1;

import interfacelayer.controller.MenuController;
import interfacelayer.view.MenuView;

/**
 *
 * @author hwkei
 */
public class Applikaasie {
    
    public static void main(String[] args) {        
        MenuController menuController = new MenuController(new MenuView());
        menuController.login();
    }
}
