/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import workshop2.domain.Account;
import workshop2.domain.AccountType;

/**
 *
 * @author hwkei
 */
@Component("AccountView")
public class AccountView {
    private static final Logger log = LoggerFactory.getLogger(AccountView.class);
    Scanner input;
    
     // Default public constructor will use System.in
    public AccountView() {
        input = new Scanner(System.in);
    }
    
    // Package private constructor can be injected with Scanner for testing
    AccountView(Scanner input) {
        this.input = input;
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
    
    /**
     * Methods related to the password change procedure
     */
    
    public void showChangePasswordScreen() {
        System.out.println("\nU wilt een wachtwoord wijzigen van een account");
    }
    
    public void showInvalidOldPassword() {
        System.out.println("\nHet oude wachtwoord is onjuist! De wachtwoordwijziging wordt afgebroken.");
        System.out.println("Druk op <enter> om terug te gaan naar het menu>\"");
        input.nextLine();
    }
    
    public Long requestAccountIdToUpdateInput(int accountListSize) {
        printRequestForIdToUpdateInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(accountListSize, respons)) {
            printInvalidRespons();
            printRequestForIdToUpdateInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Long.parseLong(respons) - 1;
    }
    
    public void showAccountToBeUpdated(Account account){
        System.out.println("\nU heeft aangegeven het volgende account te willen wijzigen:\n\n");
        System.out.printf("%-20s%-20s%-5s\n", "gebruikersnaam", "wachtwoord", "Account type");
        System.out.println("--------------------------------------------------");
        System.out.println(account.toStringNoId());        
        System.out.println("\n\nHierna kunt u de gebruikersnaam, het wachtwoord of het accounttype wijzigen.\n"
                + "Indien u voor een bepaald gegeven geen wijziging wenst door te voeren,\n"
                + "vul dan een sterretje in en druk op <enter>.\n");
    }
    
    public void showAccountUpdateChanges(Account accountBeforeUpdate, Account accountAfterUpdate){
        System.out.println("\nU heeft aangegeven het volgende account te willen wijzigen:\n");
        System.out.println("Geselecteerd account voor wijzigingen:\n");
        
        System.out.printf("%-20s%-20s%-5s\n", "gebruikersnaam", "wachtwoord", "Account type");
        System.out.println("--------------------------------------------------");
        System.out.println(accountBeforeUpdate.toStringNoId());
        
        System.out.println("\nGeselecteerd account na wijzigingen:\n");
        
        System.out.printf("%-20s%-20s%-5s\n", "gebruikersnaam", "wachtwoord", "Account type");
        System.out.println("--------------------------------------------------");
        System.out.println(accountAfterUpdate.toStringNoId());        
        }
    
    public Integer requestConfirmationToUpdate() {
        printRequestForUpdateConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            printInvalidRespons();
            printRequestForUpdateConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
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
            printInvalidRespons();
            printRequestForUsernameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
     /**
     * Returns a valid username or null if the user aborts
     * @return 
     */
    public String requestUpdateUsernameInput() {        
        printRequestForUsernameInput();
        String respons =  input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            printInvalidRespons();
            printRequestForUsernameInput();            
            respons = input.nextLine();
            if (respons.equals("*")) return null; // User initiated abort
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
            printInvalidRespons();
            printRequestForPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    /**
     * Returns a valid password or null if the user aborts
     * @return 
     */
    public String requestUpdatePasswordInput() {        
        printRequestForPasswordInput();
        String respons =  input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            printInvalidRespons();
            printRequestForPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("*")) return null; // User initiated abort
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
            printInvalidRespons();
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
            printInvalidRespons();
            printRequestForOldPasswordInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid accountType or null if the user aborts
     * @return AccountType
     */
    public AccountType requestAccountType() {
        List<AccountType> enumTypes = new ArrayList<>(EnumSet.allOf(AccountType.class));
        
        System.out.println("De beschikbare account types zijn:");
        System.out.printf("%-5s%-20s\n", "ID", "Account Type");
        System.out.println("------------------------------");
        for (int i = 0; i < enumTypes.size(); i++) {
            System.out.printf("%-10s%-30s\n", i + 1, enumTypes.get(i));
        }
        printRequestForAccountType();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(enumTypes.size(), respons)) {
            printInvalidRespons();
            printRequestForAccountType();
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }        
        return enumTypes.get(Integer.parseInt(respons) - 1);       
    }
    
    /**
     * Returns a valid accountType or null if the user aborts
     * @return Account type id
     */
    public AccountType requestUpdateAccountType() {
        List<AccountType> enumTypes = new ArrayList<>(EnumSet.allOf(AccountType.class));
        System.out.println("De beschikbare account types zijn:");
        System.out.printf("%-5s%-20s\n", "ID", "Account Type");
        System.out.println("------------------------------");
        for (int i = 0; i < enumTypes.size(); i++) {
            System.out.printf("%-10s%-30s\n", i + 1, enumTypes.get(i));
        }
        printRequestForAccountType();
        String respons =  input.nextLine();
        if (respons.equals("*")) return null; // User initiated abort
        while (!Validator.isPositiveInteger(respons)) {
            printInvalidRespons();
            printRequestForAccountType();
            respons = input.nextLine();
            if (respons.equals("*")) return null; // User initiated abort
        }
        return enumTypes.get(Integer.parseInt(respons) - 1);
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
    
    /*
     * Methods related to selectAccount
     */
    
    public Optional<Account> selectAccount(List<Account> accountList) {
        showListOfAllAccounts(accountList);
        Integer index = requestAccountIdToSelectInput(accountList.size());
        if (index == null) return Optional.empty();        
        Account account = accountList.get(index);
        //Promp for confirmation if this is indeed the customer to select
        showAccountToBeSelected(account);
        Integer confirmed = requestConfirmationToSelect();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(account);
    }
    
    Integer requestAccountIdToSelectInput(int customerListSize){
        printRequestForIdToSelectInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(customerListSize, respons)) {
            showInvalidRespons();
            printRequestForIdToSelectInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    void printRequestForIdToSelectInput() {
        System.out.println("Selecteer het gewenste account ID gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void showAccountToBeSelected(Account account) {
        System.out.println("\nU heeft het volgende account geselecteerd:\n\n");        
        System.out.printf("%-20s%-20s%-5s\n", "gebruikersnaam", "wachtwoord", "Account type");
        System.out.println("--------------------------------------------------");
        System.out.println(account.toStringNoId());
    }
    
     Integer requestConfirmationToSelect() {
        printRequestForSelectConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForSelectConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        return Integer.parseInt(respons);
    }
     
    void printRequestForSelectConfirmation() {
        System.out.println("\nWilt u dit account selecteren?");
        System.out.println("1) Account selecteren");
        System.out.println("2) Account NIET selecteren");
        System.out.print("> ");
    }    
    
    /**
     * End Methods related to selectAccount
     */
    
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
        while (!Validator.isValidConfirmation(respons)) {
            printInvalidRespons();
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
            printInvalidRespons();
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
        System.out.println("Geef gebruikersnaam gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForPasswordInput() {
        System.out.println("Geef wachtwoord gevolgd door <enter>:");
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
    
    private void printInvalidRespons() {
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
    
    private void printRequestForIdToUpdateInput() {
        System.out.println("Geef het ID van het account dat u wilt wijzigen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForUpdateConfirmation() {
        System.out.println("\n\nWilt u deze wijziging opslaan?");
        System.out.println("1) Opslaan");
        System.out.println("2) NIET opslaan, ga terug naar menu");
        System.out.print("> ");
    }
    
    /**
     * Generic methods
     */
    
    void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
   
    
    /**
     * Methods to change database settings during program flow
     */
   
    public Integer requestDatabaseTypeInput(String currentType){
        printRequestForDatabaseTypeInput(currentType);
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!(respons.equals("1") || respons.equals("2"))) {
            showInvalidRespons();
            printRequestForDatabaseTypeInput(currentType);
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        if (respons.equals(currentType)) {
            System.out.println("Huidige database type is opnieuw gekozen. De database wordt niet gewijzigd\n"
                + "Druk op <enter> om door te gaan");
            return null;
        }
        System.out.println("Database wordt gewijzigd naar type " + respons + "\n"
                + "Druk op <enter> om door te gaan");
        input.nextLine();
        return Integer.parseInt(respons);
    }

    
    private void printRequestForDatabaseTypeInput(String currentType) {
        System.out.println("Het huidige database type is " + currentType);
        System.out.println("Kies het nieuwe type dat u wenst te gebruiken gevolgd door <enter>");
        System.out.println("Let op! Een andere database bevat mogelijk andere gegevens!:\n"
                + "Kies !<enter> om terug te gaan naar het menu\n");
        System.out.println("1) MySQL");
        System.out.println("2) MongoDB\n");
        System.out.print("> ");
    }
    
    public boolean requestConnectionPoolInput(boolean currentSetting) {
        printRequestForConnectionPoolInput(currentSetting);        
        String respons = input.nextLine();
        while (!(respons.equals("1") || respons.equals("2"))) {
            showInvalidRespons();
            printRequestForConnectionPoolInput(currentSetting);
            respons = input.nextLine();
        }
        boolean newSetting = currentSetting;
        if (respons.equals("1")) {
            System.out.println("Connectie pool wordt aan gezet"); 
            newSetting = true;
        }
        if (respons.equals("2")) {
            System.out.println("Connectie pool wordt uit gezet");
            newSetting = false;
        }
        System.out.println("Druk op <enter> om door te gaan");
        input.nextLine();
        return newSetting;
    }
    
    private void printRequestForConnectionPoolInput(boolean currentType) {
        String status = currentType ? "Aan" : "Uit";
        System.out.println("De huidige connectiepool instelling is: " + status);
        System.out.println("Kies de nieuwe instelling gevolgd door <enter>\n");
        System.out.println("1) Connectiepool aan");
        System.out.println("2) Connectiepool uit\n");
        System.out.print("> ");
    }

}
