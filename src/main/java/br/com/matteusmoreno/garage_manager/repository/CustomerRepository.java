package br.com.matteusmoreno.garage_manager.repository;

import br.com.matteusmoreno.garage_manager.domain.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public Customer findByUUID(UUID id) {
        return find("id", id).firstResult();
    }

    public boolean existsByCpfOrEmail(String cpf, String email) {
        return find("cpf = ?1 or email = ?2", cpf, email).firstResultOptional().isPresent();
    }
}
