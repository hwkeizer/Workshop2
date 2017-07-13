/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.view;

import java.util.List;
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
    
    public void showNewAddressContinueScreen() {
        System.out.println("Vul nu de gevraagde gegevens in. Als u een uitroepteken invult\n"
                + "wordt het toevoegen van een nieuwe adres afgebroken en gaat u terug\n"
                + "naar het menu. Al ingevulde gegevens worden dan niet bewaard!\n\n ");
    }
    
    public String requestStreetNameInput() {
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
    
    public Integer requestNumberInput() {
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
    
    public String requestAdditionInput() {
        printRequestForAdditionInput();
        String respons =  input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        // Addition value can be empty so no Validator test
        return respons;
    }
    
    public String requestPostalCodeInput() {
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
    
    public String requestCityInput() {
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
    public Integer requestAddressType(List<String> types) {
        System.out.println("De beschikbare adres types zijn:");
        System.out.printf("%-5s%-20s\n", "ID", "Adres type");
        System.out.println("------------------------------");
        for (int i = 0; i < types.size(); i++) {
            System.out.printf("%-10s%-30s\n", i + 1, types.get(i));
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
    
    public void showAddressToBeCreated(Address address){
        System.out.println("\nU heeft aangegeven het volgende adres te willen toevoegen:\n");
        
        System.out.printf("%-30s%-5s%-8s%-8s%-30s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats");
        System.out.println("--------------------------------------------------");
        System.out.println(address.toStringNoId());
    }
    
    public Integer requestConfirmationToCreate() {
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
    
    private void printRequestForCreateConfirmation() {
        System.out.println("\nWilt u dit adres echt toevoegen?");
        System.out.println("1) Adres toevoegen");
        System.out.println("2) Adres NIET toevoegen");
        System.out.print("> ");
    }
        
    /****************************************************************
    * General addressView methods not related to a specific action
    *****************************************************************/
    
    private void showInvalidRespons() {
        System.out.println("\nOngeldige waarde, probeer het opnieuw of geef !<enter> om af te breken.\n");
    }
    
    private void printRequestForStreetNameInput() {
        System.out.println("Geef de straatnaam gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForNumberInput() {
        System.out.println("Geef het huisnummer gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForAdditionInput() {
        System.out.println("Geef een eventuele toevoeging bij het huisnummer gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForPostalCodeInput() {
        System.out.println("Geef de postcode gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForCityInput() {
        System.out.println("Geef de woonplaats gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    private void printRequestForAddressType() {
        System.out.println("\ngeef het gewenste adrestype ");
        System.out.print("> ");
    }
}
