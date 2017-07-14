/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import workshop1.domain.Address;

/**
 *
 * @author hwkei
 */
public class AddressView {
    Scanner input;
    
    // Default public constructor will use System.in
    public AddressView() {
        input = new Scanner(System.in);
    }
    
    // Package private constructor can be injected with Scanner for testing
    AddressView(Scanner input) {
        this.input = input;
    }
    
    /*************************************
     * Methods related to createAddress
     ************************************/
    
    public void showNewAddressStartScreen() {
        System.out.println("\n\nU gaat een nieuwe adres aan de database toevoegen.");
        System.out.println("Een adres is altijd gekoppeld aan een klant");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst de klant waar u een adres voor wilt maken.");
        input.nextLine();
    }
    
    public Optional<Address> constructAddress(int customerId, List<String> addressTypes) {
        System.out.println("Vul nu de gevraagde adresgegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuwe adres afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
        
        String streetName = requestStreetNameInput(); 
        if (streetName == null) return Optional.empty(); // User interupted createAddress proces
        Integer number = requestNumberInput();
        if (number == null) return Optional.empty(); // User interupted createAddress proces
        String addition = requestAdditionInput();
        if (addition == null) return Optional.empty();  // User interupted createAddress proces
        String postalCode = requestPostalCodeInput();
        if (postalCode == null) return Optional.empty();  // User interupted createAddress proces
        String city = requestCityInput();
        if (city == null) return Optional.empty();  // User interupted createAddress proces
        Integer addressType = requestAddressType(addressTypes);
        if (addressType == null) return Optional.empty();  // User interupted createAccount proces
        
        // Prepare the address with the validated values and add to the database
        // The customerID is set to null initially, this must be added later
        Address address = new Address(streetName, number, addition, postalCode, city, customerId, addressType);
        showAddressToBeCreated(address);
        Integer confirmed = requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(address);
    }
    
    String requestStreetNameInput() {
        printRequestForStreetNameInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForStreetNameInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    Integer requestNumberInput() {
        printRequestForNumberInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isPositiveInteger(respons)) {
            showInvalidRespons();
            printRequestForNumberInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return Integer.parseInt(respons);
    }
    
    String requestAdditionInput() {
        printRequestForAdditionInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        // Addition value can be empty so no Validator test
        return respons;
    }
    
    String requestPostalCodeInput() {
        printRequestForPostalCodeInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForPostalCodeInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    String requestCityInput() {
        printRequestForCityInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidNameString(respons)) {
            showInvalidRespons();
            printRequestForCityInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    /**
     * Returns a valid addressType or null if the user aborts
     * @param types
     * @return Account type id
     */
    Integer requestAddressType(List<String> types) {
        System.out.println("De beschikbare adres types zijn:");
        System.out.printf("%-5s%-20s\n", "ID", "Adres type");
        System.out.println("------------------------------");
        for (int i = 0; i < types.size(); i++) {
            System.out.printf("%-5s%-20s\n", i + 1, types.get(i));
        }
        printRequestForAddressType();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isPositiveInteger(respons)) {
            showInvalidRespons();
            printRequestForAddressType();
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
    
    void showAddressToBeCreated(Address address){
        System.out.println("\nU heeft aangegeven het volgende adres te willen toevoegen:\n");
        
        System.out.printf("%-30s%-8s%-12s%-10s%-30s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats");
        System.out.println("----------------------------------------------------------------------");
        System.out.println(address.toStringNoId());
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
        System.out.println("\nWilt u dit adres echt toevoegen?");
        System.out.println("1) Adres toevoegen");
        System.out.println("2) Adres NIET toevoegen");
        System.out.print("> ");
    }
        
    /****************************************************************
    * General addressView methods not related to a specific action
    *****************************************************************/
    
    void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
    
    void printRequestForStreetNameInput() {
        System.out.println("Geef de straatnaam gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForNumberInput() {
        System.out.println("Geef het huisnummer gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForAdditionInput() {
        System.out.println("Geef een eventuele toevoeging bij het huisnummer gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForPostalCodeInput() {
        System.out.println("Geef de postcode gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForCityInput() {
        System.out.println("Geef de woonplaats gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void printRequestForAddressType() {
        System.out.println("\ngeef het gewenste adrestype ");
        System.out.print("> ");
    }
}
