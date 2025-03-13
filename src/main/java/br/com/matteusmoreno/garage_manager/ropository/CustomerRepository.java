package br.com.matteusmoreno.garage_manager.ropository;

import br.com.matteusmoreno.garage_manager.domain.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public boolean existsByCpfOrEmail(String cpf, String email) {
        return find("cpf = ?1 or email = ?2", cpf, email).firstResultOptional().isPresent();
    }
}
