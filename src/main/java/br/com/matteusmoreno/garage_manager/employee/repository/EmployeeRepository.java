package br.com.matteusmoreno.garage_manager.employee.repository;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public boolean existsByCpfOrEmail(String cpf, String email) {
        return find("cpf = ?1 or email = ?2", cpf, email).firstResultOptional().isPresent();
    }

    public boolean existsByUsername(String username) {
        return find("username", username).firstResultOptional().isPresent();
    }
}
