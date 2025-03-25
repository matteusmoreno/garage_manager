package br.com.matteusmoreno.garage_manager.repository;

import br.com.matteusmoreno.garage_manager.domain.Motorcycle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MotorcycleRepository implements PanacheRepository<Motorcycle> {
}
