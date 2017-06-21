/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.dom4j.io.SAXReader;
import org.dom4j.*;

/**
 *
 * @author thoma
 */
public class DatabaseConnectie {
    private static String databaseType;
    private static String serverNaam;
    private static String databaseNaam;
    private static String urlSuffix;    
    private static String poortNummer;
    private static String gebruiker;   
    private static String wachtwoord;
    private static String omgeving;
    private static Connection connectie;

    public static void main(String[] args){
        maakConnectie();
        
        if(connectie != null)
            System.out.println("Connectie gemaakt!");
    }
    
    public static void maakConnectie() {
        leesXML();
        String connectieString = databaseType
                + "://"
                + serverNaam 
                + ":" 
                + poortNummer
                + "/" 
                + databaseNaam 
                + urlSuffix;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                connectie = DriverManager.getConnection(connectieString, gebruiker, wachtwoord);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection." + ex.getMessage()); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
    }
    
    public static Connection getConnectie(){
        if(connectie != null){
            return connectie;
        }
        else{
            maakConnectie();
            return connectie;
        }
    }
    
    public static void leesXML(){
        SAXReader reader = new SAXReader();
        File file = new File("Database_settings.xml");
        try{
            Document document = reader.read(file);
            
            Node node;
            node = document.selectSingleNode("/database_settings/databaseType");
            databaseType = node.getText();
            
            node = document.selectSingleNode("/database_settings/serverNaam");
            serverNaam = node.getText();
            
            node = document.selectSingleNode("/database_settings/databaseNaam");
            databaseNaam = node.getText();
            
            node = document.selectSingleNode("/database_settings/urlSuffix");
            urlSuffix = node.getText();
            
            node = document.selectSingleNode("/database_settings/poortnummer");
            poortNummer = node.getText();
            
            node = document.selectSingleNode("/database_settings/gebruiker");
            gebruiker = node.getText();
            
            node = document.selectSingleNode("/database_settings/wachtwoord");
            wachtwoord = node.getText();
            
            //Check of het XML-bestand goed ingelezen wordt
            System.out.println(databaseType + "\n"
                    + urlSuffix + "\n"
                    + poortNummer + "\n"
                    + gebruiker + "\n"
                    + wachtwoord+ "\n");

        }
        catch(DocumentException e){
            System.out.println("Probleem met document");
        }
        
    }
    
}
