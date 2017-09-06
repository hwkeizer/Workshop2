/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.persistencelayer;

import java.util.List;
import java.util.Optional;
import workshop2.domain.Address;

/**
 *
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public interface AddressService extends GenericService {

    public void createAddress(Address address);

    public void updateAddress(Address address);

    public void deleteAddress(Address address);

    public Optional<Address> findAddressByCustomerId(Long id);

    public List<Address> findAllAddressByCustomerId(Long id);

}
