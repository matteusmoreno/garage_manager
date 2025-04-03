package br.com.matteusmoreno.garage_manager.service_order.service;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.service_order.repository.ServiceOrderRepository;
import br.com.matteusmoreno.garage_manager.service_order.constant.ServiceOrderStatus;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import br.com.matteusmoreno.garage_manager.service_order.request.CreateServiceOrderRequest;
import br.com.matteusmoreno.garage_manager.service_order_product.entity.ServiceOrderProduct;
import br.com.matteusmoreno.garage_manager.service_order_product.service.ServiceOrderProductService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceOrderProductService serviceOrderProductService;
    private final MeterRegistry meterRegistry;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository, MotorcycleRepository motorcycleRepository, EmployeeRepository employeeRepository, ServiceOrderProductService serviceOrderProductService, MeterRegistry meterRegistry) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.employeeRepository = employeeRepository;
        this.serviceOrderProductService = serviceOrderProductService;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public ServiceOrder createServiceOrder(CreateServiceOrderRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findByUUID(request.motorcycleId());
        Employee seller = employeeRepository.findByUUID(request.sellerId());
        Employee mechanic = employeeRepository.findByUUID(request.mechanicId());

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .motorcycle(motorcycle)
                .seller(seller)
                .mechanic(mechanic)
                .description(request.description())
                .laborPrice(request.laborPrice())
                .serviceOrderStatus(ServiceOrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        List<ServiceOrderProduct> products = request.products().stream()
                .map(req -> {
                    ServiceOrderProduct product = serviceOrderProductService.createServiceOrderProduct(req);
                    product.setServiceOrder(serviceOrder);
                    return product;
                })
                .toList();

        BigDecimal totalCost = products.stream()
                .map(ServiceOrderProduct::getFinalPrice)
                .reduce(request.laborPrice(), BigDecimal::add);

        serviceOrder.setProducts(products);
        serviceOrder.setTotalCost(totalCost);

        meterRegistry.counter("service_order_created").increment();
        serviceOrderRepository.persist(serviceOrder);

        return serviceOrder;
    }

}
