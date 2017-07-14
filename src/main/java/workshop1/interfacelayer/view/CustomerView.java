/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import workshop1.domain.Customer;

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
     * Methods related to createCustomer
     */
    
    public Optional<Customer> constructCustomer() {
        showNewCustomerScreen();
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
    
    void showNewCustomerScreen() {
        System.out.println("\n\nU gaat een nieuwe klant aan de database toevoegen.\n\n"
                + "Vul de gevraagde gegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuwe klant afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
    }

    String requestFirstNameInput() {
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

    String requestLastNameInput() {
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

    String requestPrefixInput() {
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
    
    /*
     * Methods related to deleteCustomer
     */
    
    public Optional<Customer> constructCustomerToDelete(List<Customer> customerList) {
        //int customerListSize = customerList.size();
        Integer index = requestCustomerIdToDeleteInput(customerList.size());
        if (index == null) return Optional.empty();        
        Customer customer = customerList.get(index);
        //Promp for confirmation if this is indeed the customer to delete
        showCustomerToBeDeleted(customer);
        Integer confirmed = requestConfirmationToDelete();
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
    
    Integer requestConfirmationToDelete() {
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
        
        return Integer.parseInt(respons);
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
        
    public Optional<Customer> constructCustomerToUpdate(List<Customer> customerList) {        
        Integer index = requestCustomerIdToUpdateInput(customerList.size());
        if (index == null) return Optional.empty();        
        Customer customerBeforeUpdate = customerList.get(index);        
        showCustomerToBeUpdated(customerBeforeUpdate);
        
        // request the user for values to update
        String newFirstName = requestNewFirstNameInput(); 
        if (newFirstName == null) {
            newFirstName = customerBeforeUpdate.getFirstName();
        } 
        String newLastName = requestNewLastNameInput();
        if (newLastName == null){
            newLastName = customerBeforeUpdate.getLastName();
        } 
        String newPrefix = requestNewPrefixInput();
        if (newPrefix == null){
            newPrefix = customerBeforeUpdate.getLastNamePrefix();
        }
        // The Id and accountId will not be updated       
        Customer customerAfterUpdate = new Customer(customerBeforeUpdate.getId(), 
                newFirstName, newLastName, newPrefix, customerBeforeUpdate.getAccountId());
        
        //Promp for confirmation of the selected update
        showCustomerUpdateChanges(customerBeforeUpdate, customerAfterUpdate);
        Integer confirmed = requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(customerAfterUpdate);
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
    
    Integer requestConfirmationToUpdate() {
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
    
    String requestNewFirstNameInput() {        
        printRequestForFirstNameInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    String requestNewLastNameInput() {        
        printRequestForLastNameInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    String requestNewPrefixInput() {        
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
    
    
    /****************************************************************
    * General customerView methods not related to a specific action
    *****************************************************************/
     
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
    
    /**
     * Constructs a customer based on the selection of the user from the given list
     * @param customerList
     * @return Optional<Customer>
     */
    public Optional<Customer> selectCustomer(List<Customer> customerList) {
        printRequestForIdInput();
        String respons = input.nextLine();
        if (respons.equals("!")) {
            showNoCustomerSelected();
            return Optional.empty();
        } // User initiated abort
        while (!Validator.isValidListIndex(customerList.size(), respons)) {
            showInvalidRespons();
            printRequestForIdInput();
            respons = input.nextLine();
            if (respons.equals("!")) {
                showNoCustomerSelected();
                return Optional.empty();
            }  // User initiated abort
        }        
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerList.get(Integer.parseInt(respons)-1));
        if (optionalCustomer.isPresent()) showSelectedCustomer(optionalCustomer.get());
        return optionalCustomer;
    }
    
    void showSelectedCustomer(Customer customer) {
        System.out.println("\nU heeft de volgende klant geselecteerd:\n\n");        
        System.out.printf("%-20s%-15s%-20s\n", "Voornaam", "Tussenvoegsel", "Achternaam");
        System.out.println("--------------------------------------------------");
        System.out.println(customer.toStringNoId());
    }
    
    void showNoCustomerSelected() {
        System.out.println("Er is geen gebruiker geselecteerd.");
    }
    
    void printRequestForIdInput() {
        System.out.println("Selecteer het gewenste klant ID gevolgd door <enter>:");
        System.out.print("> ");
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
