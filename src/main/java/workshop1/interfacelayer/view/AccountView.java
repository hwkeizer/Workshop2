/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.List;
import java.util.Scanner;
import workshop1.domain.Account;

/**
 *
 * @author hwkei
 */
public class AccountView {
    Scanner input;
    
     // Default public constructor will use System.in
    public AccountView() {
        input = new Scanner(System.in);
    }
    
    public void showNewAccountScreen() {
        System.out.println("\n\nU gaat een nieuw account aan de database toevoegen.\n\n"
                + "Vul de gevraagde gegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuw account afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
    }
    
    public void showDuplicateAccountError() {
        System.out.println("\nFout: U probeert een account toe te voegen dat al bestaat in de database.\n"
                + "Als u het bestaande account wilt wijzigen kies dan voor 'Wijzigen account'\n"
                + "Druk op <enter> om door te gaan>");
        input.nextLine();
    }
    
    public void showInvalidOldPassword() {
        System.out.println("\nHet oude wachtwoord is onjuist! De wachtwoordwijziging wordt afgebroken.");
        System.out.println("Druk op <enter> om terug te gaan naar het menu>\"");
        input.nextLine();
    }
    
    /**
     * Returns a valid username or null if the user aborts
     * @return 
     */
    public String requestUsernameInput() {        
        printRequestForUsernameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForUsernameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid password or null if the user aborts
     * @return 
     */
    public String requestPasswordInput() {        
        printRequestForPasswordInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid old password or null if the user aborts
     * @return 
     */
    public String requestOldPasswordInput() {        
        printRequestForOldPasswordInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForOldPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid new password or null if the user aborts
     * @return 
     */
    public String requestNewPasswordInput() {        
        printRequestForNewPasswordInput();
        String respons =  input.nextLine();
        printRepeatRequestForNewPasswordInput();
        String responsCheck = input.nextLine();
        if (!respons.equals(responsCheck)) {
            printPasswordsNotEqual();
            return null;
        }
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForOldPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid accountType or null if the user aborts
     * @param types
     * @return Account type id
     */
    public Integer requestAccountType(List<String> types) {
        System.out.println("De beschikbare account types zijn:");
        System.out.printf("%-5s%-20s\n", "ID", "Account Type");
        System.out.println("------------------------------");
        for (int i = 0; i < types.size(); i++) {
            System.out.printf("%-10s%-30s\n", i + 1, types.get(i));
        }
        printRequestForAccountType();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isPositiveInteger(respons)) {
            showInvalidRespons();
            printRequestForAccountType();
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        try {
            types.get(Integer.parseInt(respons) - 1);
            return Integer.parseInt(respons);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    public void showListOfAllAccounts(List<Account> accountList) {
        System.out.println("\nHieronder volgt een lijst met alle accounts.\n");
        System.out.printf("%-5s%-20s%-20s%-5s\n", "ID", "Gebruikersnaam", "Wachtwoord", "Account type");
        System.out.println("-------------------------------------------------------");
        int i = 1;
        for(Account account: accountList){
            String id = String.format("%-5s", i);
            System.out.println(id + account.toStringNoId());
            i++;
        }
        System.out.println("");
    }
    
    public void showAccountToBeDeleted(Account account){
        System.out.println("\nU heeft aangegeven het volgende account te willen verwijderen uit de database:\n\n");
        
        System.out.printf("%-20s%-20s%-5s\n", "gebruikersnaam", "wachtwoord", "Account type");
        System.out.println("--------------------------------------------------");
        System.out.println(account.toStringNoId());
    }
    
    public Integer requestConfirmationToDelete(Account account) {
        printRequestForConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        return Integer.parseInt(respons);
    }
    
    public Integer requestAccountIdInput(int accountListSize){
        printRequestForIdInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(accountListSize, respons)) {
            showInvalidRespons();
            printRequestForIdInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in List<Account> accountList
        return Integer.parseInt(respons) - 1;
        
    }
    
    private void printRequestForAccountType() {
        System.out.println("\ngeef het gewenste accounttype ");
        System.out.print("> ");
    }
        
    
    private void printRequestForUsernameInput() {
        System.out.println("Geef uw gebruikersnaam gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForPasswordInput() {
        System.out.println("Geef uw wachtwoord gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForOldPasswordInput() {
        System.out.println("Geef uw oude wachtwoord gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForNewPasswordInput() {
        System.out.println("Geef uw nieuwe wachtwoord gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printPasswordsNotEqual() {
        System.out.println("De nieuwe wachtwoorden komen niet overeen! Wachtwoord is niet gewijzigd.");
        System.out.print("Druk op <enter> om door te gaan");
        input.nextLine();
    }
    
    private void printRepeatRequestForNewPasswordInput() {
        System.out.println("Geef ter bevestiging nogmaals uw nieuwe wachtwoord gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
    
    private void printRequestForIdInput() {
        System.out.println("Geef het ID van het account dat u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForConfirmation() {
        System.out.println("Wilt u dit account echt verwijderen?");
        System.out.println("1) Account verwijderen");
        System.out.println("2) Account NIET verwijderen");
        System.out.print("> ");
    }
}
