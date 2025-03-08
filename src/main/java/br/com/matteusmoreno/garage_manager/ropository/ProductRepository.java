package br.com.matteusmoreno.garage_manager.ropository;

import br.com.matteusmoreno.garage_manager.domain.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
