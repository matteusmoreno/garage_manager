package br.com.matteusmoreno.garage_manager.motorcycle.repository;

import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleNotFoundException;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class MotorcycleRepository implements PanacheRepository<Motorcycle> {

    public Motorcycle findByUUID(UUID id) {
        if (find("id", id).firstResultOptional().isEmpty()) {
            throw new MotorcycleNotFoundException("Motorcycle not found");
        }
        return find("id", id).firstResult();
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return find("licensePlate = ?1", licensePlate).firstResultOptional().isPresent();
    }

    public Motorcycle findByLicensePlate(String licensePlate) {
        return find("licensePlate = ?1", licensePlate).firstResult();
    }
}
