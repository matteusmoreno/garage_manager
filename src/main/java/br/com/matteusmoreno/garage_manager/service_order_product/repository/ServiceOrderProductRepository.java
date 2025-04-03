package br.com.matteusmoreno.garage_manager.service_order_product.repository;

import br.com.matteusmoreno.garage_manager.service_order_product.entity.ServiceOrderProduct;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceOrderProductRepository implements PanacheRepository<ServiceOrderProduct> {
}
