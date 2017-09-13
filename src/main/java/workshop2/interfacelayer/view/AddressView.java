/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import workshop2.domain.Address;
import workshop2.domain.Address.AddressType;
import workshop2.domain.Customer;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */
@Component
public class AddressView {
    Scanner input;
    Address address = new Address();
    // Default public constructor will use System.in
    public AddressView() {
        input = new Scanner(System.in);
    }
    
    // Package private constructor can be injected with Scanner for testing
    AddressView(Scanner input) {
        this.input = input;
    }
    
    /*
     * Methods related to constructAddress
     */
    
    public void showConstructAddressStartScreen() {
        System.out.println("\n\nU gaat een nieuw adres aan de database toevoegen.");
        System.out.println("Een adres is altijd gekoppeld aan een klant.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst de\n"
                + "klant waar u een adres voor wilt maken.");
        input.nextLine();
    }
    
    public Optional<Address> constructAddress(Customer customer, List<String> addressTypes) {
        //Customer customer = null;
        System.out.println("Vul nu de gevraagde adresgegevens in. Let op dat u slechts één adres\n"
                + "van elk type per klant kunt hebben. Als u een uitroepteken invult\n"
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
        Integer addressTypeId = requestAddressType(addressTypes);
        AddressType addressType = null;
        if (addressTypeId == 1){
           address.setAddressType(AddressType.POSTADRES);
        addressType = address.getAddressType();}
        else if (addressTypeId == 2){
            address.setAddressType(AddressType.FACTUURADRES);
            addressType = address.getAddressType();
        }
          else if (addressTypeId == 3){
            address.setAddressType(AddressType.BEZORGADRES);
            addressType = address.getAddressType();
        }
        if (addressType == null) return Optional.empty();  // User interupted createAccount proces
        
        // Prepare the address with the validated values and add to the database
        // The customerID is set to null initially, this must be added later
        Address address = new Address(streetName, number, addition, postalCode, city, customer, addressType);
        showAddressToBeConstructed(address);
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
        while (!Validator.isValidPostalCode(respons)) {
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
    
    void showAddressToBeConstructed(Address address){
        System.out.println("\nU heeft aangegeven het volgende adres te willen toevoegen:\n");
        
        System.out.printf("%-30s%-8s%-12s%-10s%-20s%-20s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println(address.toStringNoId());
    }
    
    Integer requestConfirmationToCreate() {
        printRequestForConstructConfirmation();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidInt(respons) &&
                (Integer.parseInt(respons) == 1 || Integer.parseInt(respons) == 2)) {
            showInvalidRespons();
            printRequestForConstructConfirmation();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }
        
        return Integer.parseInt(respons);
    }
    
    void printRequestForConstructConfirmation() {
        System.out.println("\nWilt u dit adres echt toevoegen?");
        System.out.println("1) Adres toevoegen");
        System.out.println("2) Adres NIET toevoegen");
        System.out.print("> ");
    }
    
    /*
     * Methods related to deleteAddress
     */
    
    public void showDeleteAddressStartScreen() {
        System.out.println("\n\nU gaat een adres uit de database verwijderen.");
        System.out.println("Een adres is altijd gekoppeld aan een klant.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst de klant waar u een adres van wilt verwijderen.");
        input.nextLine();
    }
    
    
    public Optional<Address> selectAddressToDelete(List<Address> addressList) {
        showListOfAddresses(addressList);
        Integer index = requestAddressIdToDeleteInput(addressList.size());
        if (index == null) return Optional.empty();        
        Address address = addressList.get(index);
        //Promp for confirmation if this is indeed the address to delete
        showAddressToBeDeleted(address);
        Integer confirmed = requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(address);
    }
    
    
    Integer requestAddressIdToDeleteInput(int listSize) {
        printRequestForIdToDeleteInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(listSize, respons)) {
            showInvalidRespons();
            printRequestForIdToDeleteInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    void printRequestForIdToDeleteInput() {
        System.out.println("Geef het ID van het adres dat u wilt verwijderen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void showAddressToBeDeleted(Address address) {
        System.out.println("\nU heeft aangegeven het volgende adres te willen verwijderen:\n\n");        
        System.out.printf("%-30s%-8s%-12s%-10s%-20s%-5s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println(address.toStringNoId());
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
   
    void printRequestForDeleteConfirmation() {
        System.out.println("Wilt u dit adres echt verwijderen?");
        System.out.println("1) Adres verwijderen");
        System.out.println("2) Adres NIET verwijderen");
        System.out.print("> ");
    }    
    
    /*
     * Methods related to updateAddress
     */
    
    public void showUpdateAddressStartScreen() {
        System.out.println("\n\nU gaat een adres uit de database aanpassen.");
        System.out.println("Een adres is altijd gekoppeld aan een klant.");
        System.out.println("Druk op <enter> en selecteer in het volgende scherm eerst \n"
                + "de klant waar u een adres van wilt aanpassen.");
        input.nextLine();
    }
    
    public Optional<Address> selectAddressToUpdate(List<Address> addressList) {
        showListOfAddresses(addressList);
        Address addressBeforeUpdate;
        if (addressList.isEmpty()) return Optional.empty();
        if (addressList.size() > 1) {
            Integer index = requestAddressIdToUpdateInput(addressList.size());
            if (index == null) return Optional.empty();        
            addressBeforeUpdate = addressList.get(index);
        } else {
            addressBeforeUpdate = addressList.get(0);
        }
          
        showAddressToBeUpdated(addressBeforeUpdate);
        
        // request the user for values to update
        String newStreetName = requestUpdateStreetNameInput(); 
        if (newStreetName == null) {
            newStreetName = addressBeforeUpdate.getStreetName();
        } 
        Integer newNumber = requestUpdateNumberInput();
        if (newNumber == null){
            newNumber = addressBeforeUpdate.getNumber();
        } 
        String newAddition = requestUpdateAdditionInput();
        if (newAddition == null){
            newAddition = addressBeforeUpdate.getAddition();
        }
        String newPostalCode = requestUpdatePostalCodeInput();
        if (newPostalCode == null){
            newPostalCode = addressBeforeUpdate.getPostalCode();
        }
        String newCity = requestUpdateCityInput();
        if (newCity == null){
            newCity = addressBeforeUpdate.getCity();
        }
        // The Id, customerId and address type Id will not be updated       
        Address addressAfterUpdate = new Address(addressBeforeUpdate.getId(), 
                newStreetName, newNumber, newAddition, newPostalCode, newCity,
                addressBeforeUpdate.getCustomer(), addressBeforeUpdate.getAddressType());
        
        //Promp for confirmation of the selected update
        showAddressUpdateChanges(addressBeforeUpdate, addressAfterUpdate);        
        Integer confirmed = requestConfirmationToUpdate();
        if (confirmed == null || confirmed == 2){
            return Optional.empty();
        }
        return Optional.ofNullable(addressAfterUpdate);
    }
    
    String requestUpdateStreetNameInput() {
        printRequestForStreetNameInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    Integer requestUpdateNumberInput() {
        printRequestForNumberInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        while (!Validator.isPositiveInteger(respons)) {
            showInvalidRespons();
            printRequestForNumberInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return Integer.parseInt(respons);
    }
    
    String requestUpdateAdditionInput() {
        printRequestForAdditionInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }
    
    String requestUpdatePostalCodeInput() {
        printRequestForPostalCodeInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
         while (!Validator.isValidPostalCode(respons)) {
            showInvalidRespons();
            printRequestForPostalCodeInput();            
            respons = input.nextLine();
            if (respons.equals("!")) return null; // User initiated abort
        }
        return respons;
    }
    
    String requestUpdateCityInput() {
        printRequestForCityInput();
        String respons =  input.nextLine();
        if (respons.isEmpty()) return null; // User initiated abort
        return respons;
    }        
    
    
    Integer requestAddressIdToUpdateInput(int listSize) {
        printRequestForIdToUpdateInput();
        String respons = input.nextLine();
        if (respons.equals("!")) return null; // User initiated abort
        while (!Validator.isValidListIndex(listSize, respons)) {
            showInvalidRespons();
            printRequestForIdToUpdateInput();
            respons = input.nextLine();
            if (respons.equals("!")) return null;  // User initiated abort
        }        
        //index of product in ArrayList<Product> productList
        return Integer.parseInt(respons) - 1;
    }
    
    void printRequestForIdToUpdateInput() {
        System.out.println("Geef het ID van het adres dat u wilt aanpassen gevolgd door <enter>:");
        System.out.print("> ");
    }
    
    void showAddressToBeUpdated(Address address) {
        System.out.println("\nU heeft aangegeven het volgende adres te willen aanpassen:\n\n");        
        System.out.printf("%-30s%-8s%-12s%-10s%-20s%-5s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println(address.toStringNoId());
    }
    
    void showAddressUpdateChanges(Address before, Address after) {
        System.out.println("\nU heeft aangegeven het volgende adres te willen wijzigen:\n");
        System.out.println("Geselecteerd adres voor wijzigingen:\n");
        
        System.out.printf("%-30s%-8s%-12s%-10s%-20s%-5s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println(before.toStringNoId());
        
        System.out.println("\nGeselecteerd adres na wijzigingen:\n");
        
        System.out.printf("%-30s%-8s%-12s%-10s%-20s%-5s\n", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println(after.toStringNoId());
    }
     
    Integer requestConfirmationToUpdate() {
        printRequestForUpdateConfirmation();
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
   
    void printRequestForUpdateConfirmation() {
        System.out.println("Wilt u dit adres echt aanpassen?");
        System.out.println("1) Adres aanpassen");
        System.out.println("2) Adres NIET aanpassen");
        System.out.print("> ");
    }    
        
    /*
     * General addressView methods not related to a specific action
     */
    
    public void showListOfAddresses(List<Address> addressList) {
        System.out.println("\nDe volgende adressen horen bij deze klant:\n");
        System.out.printf("%-5s%-30s%-8s%-12s%-10s%-20s%-5s\n", "ID", "Straatnaam", "Nummer", "Toevoeging", "Postcode", "Plaats", "Type");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        int i = 1;
        for(Address address: addressList){
            String id = String.format("%-5d", i);
            System.out.println(id + address.toStringNoId());
            i++;
        }
        System.out.println("");
    }
    
    public void showNoAddressCanBeAdded() {
        System.out.println("Deze klant heeft al een postadres, factuuradres en bezorgadres. Er kan geen\n"
                + "adres meer worden toegevoegd. U kunt alleen één van de bestaande\n"
                + "adressen wijzigen. Kies hiervoor `Adres aanpassen` in het menu.\n"
                + "Druk op <enter> om door te gaan\n");
        input.nextLine();
    }
    
    public void showAddressTypeExists() {
        System.out.println("Dit adres type bestaat al voor deze klant. Kies `Adres aanpassen` indien\n"
                + "u het bestaande adres wilt wijzigen of kies het juiste adres type\n"
                + "om dit adres aan de klant toe te voegen\n"
                + "Druk op <enter> om verder te gaan");
        input.nextLine();
    }
    
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
