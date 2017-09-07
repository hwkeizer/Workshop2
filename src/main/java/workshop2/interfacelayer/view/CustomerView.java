/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import workshop2.domain.Customer;

/**
 *
 * @author hwkei
 */
public class CustomerView {
    Scanner input;
    
    // Default public constructor will use System.in
    public CustomerView() {
        input = new Scanner(System.in);
    }
    
    // Package private constructor can be injected with Scanner for testing
    CustomerView(Scanner input) {
        this.input = input;
    }
    
    /*
     * Methods related to constructCustomer
     */
    
    public Optional<Customer> constructCustomer() {
        showConstructCustomerScreen();
        String firstName = requestFirstNameInput(); 
        if (firstName == null) return Optional.empty(); // User interupted createCustomer proces
        String lastName = requestLastNameInput();
        if (lastName == null) return Optional.empty(); // User interupted createCustomer proces
        String prefix = requestPrefixInput();
        if (prefix == null) return Optional.empty();  // User interupted createCustomer proces
        
        // The accountID is set to null initially, this must be added later
        Customer customer = new Customer(firstName, lastName, prefix, null);
        showCustomerToBeCreated(customer);
        Integer confirmed = requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(customer);
    }
    
    void showConstructCustomerScreen() {
        System.out.println("\n\nU gaat een nieuwe klant aan de database toevoegen.\n\n"
                + "Vul de gevraagde gegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuwe klant afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
    }

    public String requestFirstNameInput() {
        printRequestForFirstNameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForFirstNameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }

    public String requestLastNameInput() {
        printRequestForLastNameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForLastNameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }

    public String requestPrefixInput() {
        printRequestForPrefixInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort 
        // Prefix value can be empty so no Validator test
        return respons;
    }
    
    void showCustomerToBeCreated(Customer customer){
        System.out.println("\nU heeft aangegeven de volgende klant te willen toevoegen:\n");
        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
    }
 
    Integer requestConfirmationToCreate() {
        printRequestForCreateConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForCreateConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    void printRequestForCreateConfirmation() {
        System.out.println("\nWilt u deze klant echt toevoegen?");
        System.out.println("1) Klant toevoegen");
        System.out.println("2) Klant NIET toevoegen");
        System.out.print("> ");
    }
    
     /**
     * Methods related to linkAccountToCustomer
     */
    
    public void showLinkAccountToCustomerScreen() {
        System.out.println("\nU wilt een account aan een klant koppelen. Beiden moeten bestaan\n"
                + "in het systeem en mogen geen andere koppeling hebben!\n"
                + "Druk op <enter> om een klant te selecteren\n");
        input.nextLine();
    }
    
    public void showCustomerHasAlreadyAccount() {
        System.out.println("\nDe geselecteerde klant haaft al een account. verwijder dit\n"
                + "account eerst voordat u een nieuw account aan deze klant kan koppelen\n"
                + "Druk op <enter> om door te gaan");
        input.nextLine();
    }
    
    public void showAccountIsAlreadyInUse(Customer customer) {
        System.out.println("\nHet geselecteerde account hoort al bij de volgende klant: \n");
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
        System.out.println("Kies een ander account of maak eerst een nieuw account voor deze klant\n"
                + "Druk op <enter> om door te gaan");
        input.nextLine();
    }
    
    
    /*
     * Methods related to deleteCustomer
     */
    
    public Optional<Customer> selectCustomerToDelete(List<Customer> customerList) {
        showListOfAllCustomers(customerList);
        Integer index = requestCustomerIdToDeleteInput(customerList.size());
        if (index == null) return Optional.empty();        
        Customer customer = customerList.get(index);
        //Promp for confirmation if this is indeed the customer to delete
        showCustomerToBeDeleted(customer);
        Long confirmed = requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(customer);
    }
    
    void showCustomerToBeDeleted(Customer customer) {
        System.out.println("\nU heeft aangegeven de volgende klant te willen verwijderen:\n\n");        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
    }
    
    Integer requestCustomerIdToDeleteInput(int customerListSize) {
        printRequestForIdToDeleteInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(customerListSize, respons)) {
            showInvalidRespons();
            printRequestForIdToDeleteInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public Long requestConfirmationToDelete() {
        printRequestForDeleteConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForDeleteConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Long.parseLong(respons);
    }
    
    void printRequestForIdToDeleteInput() {
        System.out.println("Geef het ID van de klant die u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForDeleteConfirmation() {
        System.out.println("Wilt u deze klant echt verwijderen?");
        System.out.println("1) Klant verwijderen");
        System.out.println("2) Klant NIET verwijderen");
        System.out.print("> ");
    }    
     
    /*
     * Methods related to updateCustomer
     */
        
    public Optional<Customer> selectCustomerToUpdate(List<Customer> customerList) { 
        showListOfAllCustomers(customerList);
        Integer index = requestCustomerIdToUpdateInput(customerList.size());
        if (index == null) return Optional.empty();        
        Customer customer = customerList.get(index);        
        showCustomerToBeUpdated(customer);
        
        // request the user for values to update
        String newFirstName = requestUpdateFirstNameInput(); 
        if (newFirstName == null) {
            newFirstName = customer.getFirstName();
        } 
        String newLastName = requestUpdateLastNameInput();
        if (newLastName == null){
            newLastName = customer.getLastName();
        } 
        String newPrefix = requestUpdatePrefixInput();
        if (newPrefix == null){
            newPrefix = customer.getLastNamePrefix();
        }
        // The Id and accountId will not be updated       
        Customer tmpCustomer = new Customer(newFirstName, newLastName, newPrefix, customer.getAccount());
        
        //Promp for confirmation of the selected update
        showCustomerUpdateChanges(customer, tmpCustomer);
        Integer confirmed = requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        // Change the values except for Id and accountId
        customer.setFirstName(tmpCustomer.getFirstName());
        customer.setLastName(tmpCustomer.getLastName());
        customer.setLastNamePrefix(tmpCustomer.getLastNamePrefix());
        return Optional.ofNullable(customer);
    }
    
    void showCustomerToBeUpdated(Customer customer) {
        System.out.println("\nU heeft aangegeven de volgende klant te willen wijzigen:\n\n");        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
        
        System.out.println("\n\nHierna kunt u de voornaam, het tussenvoegsel of de achternaam wijzigen.\n"
                + "Indien u voor een bepaald gegeven geen wijziging wenst door te voeren,\n"
                + "laat het veld dan leeg en druk op <enter>.\n");
    }
    
    void showCustomerUpdateChanges(Customer customerBeforeUpdate, Customer customerAfterUpdate){
        System.out.println("\nU heeft aangegeven de volgende klant te willen wijzigen:\n");
        System.out.println("Geselecteerde klant voor wijzigingen:\n");
        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customerBeforeUpdate.toStringNoId());
        
        System.out.println("\nGeselecteerde klant na wijzigingen:\n");
        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customerAfterUpdate.toStringNoId());
        
    }
    
    Integer requestCustomerIdToUpdateInput(int customerListSize) {
        printRequestForIdToUpdateInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(customerListSize, respons)) {
            showInvalidRespons();
            printRequestForIdToUpdateInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    public Integer requestConfirmationToUpdate() {
        printRequestForUpdateConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForUpdateConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    String requestUpdateFirstNameInput() {        
        printRequestForFirstNameInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    String requestUpdateLastNameInput() {        
        printRequestForLastNameInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    String requestUpdatePrefixInput() {        
        printRequestForPrefixInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    void printRequestForIdToUpdateInput() {
        System.out.println("Geef het ID van de klant die u wilt wijzigen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForUpdateConfirmation() {
        System.out.println("\n\nWilt u deze wijziging opslaan?");
        System.out.println("1) Opslaan");
        System.out.println("2) NIET opslaan, ga terug naar menu");
        System.out.print("> ");
    }
    
    /*
     * Methods related to selectCustomer
     */
    
    /**
     * Select a customer based on the selection of the user from the given list
     * @param customerList
     * @return Optional<Customer>
     */
    public Optional<Customer> selectCustomer(List<Customer> customerList) {
        showListOfAllCustomers(customerList);
        Integer index = requestCustomerIdToSelectInput(customerList.size());
        if (index == null) return Optional.empty();        
        Customer customer = customerList.get(index);
        //Promp for confirmation if this is indeed the customer to select
        showCustomerToBeSelected(customer);
        Integer confirmed = requestConfirmationToSelect();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(customer);
    }
    
    Integer requestCustomerIdToSelectInput(int customerListSize){
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
        System.out.println("Selecteer het gewenste klant ID gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void showCustomerToBeSelected(Customer customer) {
        System.out.println("\nU heeft de volgende klant geselecteerd:\n\n");        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
    }
    
    Integer requestConfirmationToSelect() {
        printRequestForSelectConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForDeleteConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    void printRequestForSelectConfirmation() {
        System.out.println("\nWilt u deze klant selecteren?");
        System.out.println("1) Klant selecteren");
        System.out.println("2) Klant NIET selecteren");
        System.out.print("> ");
    }  
    
    /*
    * General customerView methods not related to a specific action
    */
     
    public void showListOfAllCustomers(List<Customer> customerList) {
        System.out.println("\nHieronder volgt een lijst met alle klanten.\n");
        System.out.printf("%-5s%-20s%-15s%-20s\n", "ID", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("-------------------------------------------------------");
        
        int i = 1;
        for(Customer customer: customerList){
            String id = String.format("%-5d", i);
            System.out.println(id + customer.toStringNoId());
            i++;
        }
        System.out.println("");
    }
    
        void showInvalidRespons() {
            System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
        }

    void printRequestForFirstNameInput() {
        System.out.println("Geef de voornaam van de klant gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForLastNameInput() {
        System.out.println("Geef de achternaam van de klant gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForPrefixInput() {
        System.out.println("Geef een eventueel tussenvoegsel van de klant gevolgd door <enter>:");
        System.out.print("> ");
    }   
   
}
