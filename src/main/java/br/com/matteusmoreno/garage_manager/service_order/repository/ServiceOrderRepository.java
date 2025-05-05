package br.com.matteusmoreno.garage_manager.service_order.repository;

import br.com.matteusmoreno.garage_manager.exception.exception_class.ServiceOrderNotFoundException;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ServiceOrderRepository implements PanacheRepository<ServiceOrder> {

    public ServiceOrder findById(Long id) {
        if (find("id", id).firstResultOptional().isEmpty()) {
            throw new ServiceOrderNotFoundException("Service order not found");
        }
        return find("id", id).firstResult();
    }

    public List<ServiceOrder> findAllServiceOrders() {
        return listAll();
    }

    public List<ServiceOrder> findByDate(int day, int month, int year) {
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime end = start.plusDays(1);

        return find("createdAt >= ?1 and createdAt < ?2", start, end).list();
    }

    public List<ServiceOrder> findByMonthAndYear(int month, int year) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        return find("createdAt >= ?1 and createdAt < ?2", start, end).list();
    }

    public List<ServiceOrder> findByYear(int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = start.plusYears(1);

        return find("createdAt >= ?1 and createdAt < ?2", start, end).list();
    }


}
