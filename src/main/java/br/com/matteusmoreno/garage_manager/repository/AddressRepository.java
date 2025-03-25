package br.com.matteusmoreno.garage_manager.repository;

import br.com.matteusmoreno.garage_manager.domain.Address;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {

    public Address findByZipCodeAndNumber(String zipCode, String number) {
        return find("zipCode = ?1 and number = ?2", zipCode, number).firstResult();
    }
}
