/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer;

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
    private final MenuActions action;
    private final boolean isAction;
    
    
    public MenuItem(MenuItem parent, int id, String name, MenuActions action, boolean isAction) {
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

    public String getName() {
        return name;
    }

    public MenuActions getAction() {
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
    
    public void printItem() {        
        System.out.println(getItemChoice() + ") " + name);
    }
    
    public String getItemChoice() {
        String number = ((Integer)id).toString();
        number = number.substring(number.length()-1);
        return number;
    }
    
    public MenuItem getMainScreen() {
        MenuItem item = this;
        while (!item.getName().equals("Hoofdscherm")) {
            System.out.println("NAAM: " + item.getName());
            item = item.getParent();
        }
        return item;
    }
    
    public boolean isMainScreen() {
        return name.equals("Hoofdscherm");
    }
}