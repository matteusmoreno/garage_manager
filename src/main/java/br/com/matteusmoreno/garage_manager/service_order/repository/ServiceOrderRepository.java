package br.com.matteusmoreno.garage_manager.service_order.repository;

import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceOrderRepository implements PanacheRepository<ServiceOrder> {
}
