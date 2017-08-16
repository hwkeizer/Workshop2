/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hwkei
 */
public class MenuItem {
    private final MenuItem parentMenu;
    private final List<MenuItem> subMenu;
    private final int id;
    private final String name;
    private final MenuAction action;
    private final boolean isAction;
    
    
    public MenuItem(MenuItem parent, int id, String name, MenuAction action, boolean isAction) {
        this.subMenu = new ArrayList<>();
        this.parentMenu = parent;
        this.id = id;
        this.name = name;
        this.action = action;
        this.isAction = isAction;
    }
    
    public void addSubMenu(MenuItem menuItem) {
        subMenu.add(menuItem);
    }
    
    public String getItemChoice() {
        String number = ((Integer)id).toString();
        number = number.substring(number.length()-1);
        return number;
    }
    
    public MenuItem getMainScreen() {
        MenuItem item = this;
        while (!item.getName().equals("Hoofdscherm")) {
            item = item.getParent();
        }
        return item;
    }
    
    public boolean isMainScreen() {
        return name.equals("Hoofdscherm");
    }
    
    public void printItem() {        
        System.out.println(getItemChoice() + ") " + name);
    }

    public String getName() {
        return name;
    }

    public MenuAction getAction() {
        return action;
    }

    public List<MenuItem> getSubMenu() {
        return subMenu;
    }

    public MenuItem getParent() {
        return parentMenu;
    }

    public boolean isAction() {
        return isAction;
    }
    
   
}