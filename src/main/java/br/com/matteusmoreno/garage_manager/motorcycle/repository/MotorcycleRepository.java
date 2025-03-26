package br.com.matteusmoreno.garage_manager.motorcycle.repository;

import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MotorcycleRepository implements PanacheRepository<Motorcycle> {

    public boolean findByLicensePlate(String licensePlate) {
        return find("licensePlate = ?1", licensePlate).firstResultOptional().isPresent();
    }
}
