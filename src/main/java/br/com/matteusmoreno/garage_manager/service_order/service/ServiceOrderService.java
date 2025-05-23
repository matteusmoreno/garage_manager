package br.com.matteusmoreno.garage_manager.service_order.service;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.exception.exception_class.InvalidServiceOrderStatusException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ServiceOrderNotFoundException;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.service_order.repository.ServiceOrderRepository;
import br.com.matteusmoreno.garage_manager.service_order.constant.ServiceOrderStatus;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import br.com.matteusmoreno.garage_manager.service_order.request.CreateServiceOrderRequest;
import br.com.matteusmoreno.garage_manager.service_order.request.UpdateServiceOrderRequest;
import br.com.matteusmoreno.garage_manager.service_order.response.ServiceOrderDetailsResponse;
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

    @Transactional
    public List<ServiceOrderDetailsResponse> findAllServiceOrder() {
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findAllServiceOrders();

        List<ServiceOrderDetailsResponse> servicerOrdersResponse = serviceOrders.stream()
                .map(ServiceOrderDetailsResponse::new)
                .toList();

        meterRegistry.counter("all_service_orders_found").increment();
        return servicerOrdersResponse;
    }

    @Transactional
    public List<ServiceOrderDetailsResponse> findServiceOrdersByDay(int day, int month, int year) {
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByDate(day, month, year);

        List<ServiceOrderDetailsResponse> servicerOrdersResponse = serviceOrders.stream()
                .map(ServiceOrderDetailsResponse::new)
                .toList();

        meterRegistry.counter("service_orders_found_by_day").increment();
        return servicerOrdersResponse;
    }

    @Transactional
    public List<ServiceOrderDetailsResponse> findServiceOrdersByMonth(int month, int year) {
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByMonthAndYear(month, year);

        List<ServiceOrderDetailsResponse> servicerOrdersResponse = serviceOrders.stream()
                .map(ServiceOrderDetailsResponse::new)
                .toList();

        meterRegistry.counter("service_orders_found_by_month").increment();
        return servicerOrdersResponse;
    }

    @Transactional
    public List<ServiceOrderDetailsResponse> findServiceOrdersByYear(int year) {
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByYear(year);

        List<ServiceOrderDetailsResponse> servicerOrdersResponse = serviceOrders.stream()
                .map(ServiceOrderDetailsResponse::new)
                .toList();

        meterRegistry.counter("service_orders_found_by_year").increment();
        return servicerOrdersResponse;
    }

    @Transactional
    public ServiceOrder findServiceOrderById(Long id) {
        if (serviceOrderRepository.findById(id) == null) {
            meterRegistry.counter("service_order_not_found").increment();
            throw new ServiceOrderNotFoundException("Service order not found");
        }
        meterRegistry.counter("service_order_found_by_id").increment();
        return serviceOrderRepository.findById(id);
    }

    @Transactional
    public ServiceOrder updateServiceOrder(UpdateServiceOrderRequest request) {
        ServiceOrder serviceOrder = findServiceOrderById(request.id());

        if (request.motorcycleId() != null) {
            Motorcycle motorcycle = motorcycleRepository.findByUUID(request.motorcycleId());
            serviceOrder.setMotorcycle(motorcycle);
        }

        if (request.sellerId() != null) {
            Employee seller = employeeRepository.findByUUID(request.sellerId());
            serviceOrder.setSeller(seller);
        }

        if (request.mechanicId() != null) {
            Employee mechanic = employeeRepository.findByUUID(request.mechanicId());
            serviceOrder.setMechanic(mechanic);
        }

        if (request.products() != null) {
            serviceOrder.getProducts().clear();
            List<ServiceOrderProduct> products = request.products().stream()
                    .map(req -> {
                        ServiceOrderProduct product = serviceOrderProductService.createServiceOrderProduct(req);
                        product.setServiceOrder(serviceOrder);
                        return product;
                    })
                    .toList();

            serviceOrder.getProducts().addAll(products);
            BigDecimal totalCost = products.stream()
                    .map(ServiceOrderProduct::getFinalPrice)
                    .reduce(serviceOrder.getLaborPrice(), BigDecimal::add);

            serviceOrder.setTotalCost(totalCost);
        }

        if (request.description() != null) {
            serviceOrder.setDescription(request.description());
        }

        if (request.laborPrice() != null) {
            serviceOrder.setLaborPrice(request.laborPrice());
            BigDecimal totalCost = serviceOrder.getProducts().stream()
                    .map(ServiceOrderProduct::getFinalPrice)
                    .reduce(request.laborPrice(), BigDecimal::add);
            serviceOrder.setTotalCost(totalCost);
        }

        serviceOrder.setUpdatedAt(LocalDateTime.now());
        meterRegistry.counter("service_order_updated").increment();
        serviceOrderRepository.persist(serviceOrder);

        return serviceOrder;
    }

    @Transactional
    public ServiceOrder startServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id);

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.PENDING) {
            throw new InvalidServiceOrderStatusException("Service order is not in a state to be started");
        }

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.IN_PROGRESS);
        serviceOrder.setStartedAt(LocalDateTime.now());

        meterRegistry.counter("service_order_started").increment();
        serviceOrderRepository.persist(serviceOrder);
        return serviceOrder;
    }

    @Transactional
    public ServiceOrder completeServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id);

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.IN_PROGRESS) {
            throw new InvalidServiceOrderStatusException("Service order is not in a state to be completed");
        }

        serviceOrder.setFinishedAt(LocalDateTime.now());
        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.COMPLETED);

        meterRegistry.counter("service_order_completed").increment();
        serviceOrderRepository.persist(serviceOrder);
        return serviceOrder;
    }

    @Transactional
    public ServiceOrder cancelServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id);

        if (serviceOrder.getServiceOrderStatus() == ServiceOrderStatus.CANCELED) {
            throw new InvalidServiceOrderStatusException("Service order is already canceled");
        }

        serviceOrder.setCanceledAt(LocalDateTime.now());
        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.CANCELED);

        meterRegistry.counter("service_order_canceled").increment();
        serviceOrderRepository.persist(serviceOrder);
        return serviceOrder;
    }
}
