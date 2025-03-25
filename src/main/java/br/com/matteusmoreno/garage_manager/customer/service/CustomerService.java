package br.com.matteusmoreno.garage_manager.customer.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.customer.repository.CustomerRepository;
import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.customer.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.customer.request.UpdateCustomerRequest;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.exception.exception_class.*;
import br.com.matteusmoreno.garage_manager.address.service.AddressService;
import br.com.matteusmoreno.garage_manager.motorcycle.service.MotorcycleService;
import br.com.matteusmoreno.garage_manager.utils.UtilsService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final MeterRegistry meterRegistry;
    private final UtilsService utilsService;
    private final MotorcycleService motorcycleService;

    public CustomerService(CustomerRepository customerRepository, AddressService addressService, MeterRegistry meterRegistry, UtilsService utilsService, MotorcycleService motorcycleService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.meterRegistry = meterRegistry;
        this.utilsService = utilsService;
        this.motorcycleService = motorcycleService;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        if (!utilsService.cpfValidation(request.cpf())) {
            throw new CpfInvalidException("Invalid CPF");
        }

        if (!utilsService.dateValidation(request.birthDate())) {
            throw new InvalidDateException("Invalid date");
        }

        if (customerRepository.existsByCpfOrEmail(request.cpf(), request.email())) {
            meterRegistry.counter("customer_exists").increment();
            throw new CustomerAlreadyExistsException("Customer already exists with the provided CPF or email");
        }

        Address address = addressService.createAddress(request.zipCode(), request.addressNumber(), request.addressComplement());
        Integer age = utilsService.calculateAge(request.birthDate());

        Customer customer = Customer.builder()
                .name(request.name())
                .birthDate(request.birthDate())
                .age(age)
                .phone(request.phone())
                .email(request.email())
                .cpf(request.cpf())
                .address(address)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)
                .build();

        List<Motorcycle> motorcycles = motorcycleService.createMotorcycle(request.motorcycles(), customer);
        customer.setMotorcycles(motorcycles);

        meterRegistry.counter("customer_created").increment();
        customerRepository.persist(customer);

        return customer;
    }

    @Transactional
    public Customer findCustomerById(UUID id) {
        if (customerRepository.findByUUID(id) == null) {
            meterRegistry.counter("customer_not_found").increment();
            throw new CustomerNotFoundException("Customer not found");
        }

        Customer customer = customerRepository.findByUUID(id);
        meterRegistry.counter("customer_found_by_id").increment();

        return customer;
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        Customer customer = findCustomerById(request.id());

        if (request.name() != null) customer.setName(request.name());
        if (request.birthDate() != null) {
            if (!utilsService.dateValidation(request.birthDate())) {
                throw new InvalidDateException("Invalid date");
            }
            customer.setBirthDate(request.birthDate());
            customer.setAge(utilsService.calculateAge(request.birthDate()));
        }
        if (request.phone() != null) customer.setPhone(request.phone());
        if (request.email() != null) customer.setEmail(request.email());
        if (request.cpf() != null){
            if (!utilsService.cpfValidation(request.cpf())) {
                throw new CpfInvalidException("Invalid CPF");
            }
            customer.setCpf(request.cpf());
        }
        if (request.zipCode() != null) {
            Address address = addressService.createAddress(request.zipCode(), request.addressNumber(), request.addressComplement());
            customer.setAddress(address);
        }
        if (request.addressNumber() != null) customer.getAddress().setNumber(request.addressNumber());
        if (request.addressComplement() != null) customer.getAddress().setComplement(request.addressComplement());

        customer.setUpdatedAt(LocalDateTime.now());
        meterRegistry.counter("customer_updated").increment();

        customerRepository.persist(customer);

        return customer;
    }

    @Transactional
    public void disableCustomerById(UUID id) {
        Customer customer = findCustomerById(id);

        if (!customer.getIsActive()) {
            meterRegistry.counter("customer_already_disabled").increment();
            throw new CustomerIsAlreadyDisabledException("Customer is already disabled");
        }

        customer.setDeletedAt(LocalDateTime.now());
        customer.setIsActive(false);
        meterRegistry.counter("customer_deleted").increment();

        customerRepository.persist(customer);
    }

    @Transactional
    public Customer enableCustomerById(UUID id) {
        Customer customer = findCustomerById(id);

        if (customer.getIsActive()) {
            meterRegistry.counter("customer_already_enabled").increment();
            throw new CustomerIsAlreadyEnabledException("Customer is already enabled");
        }

        customer.setUpdatedAt(LocalDateTime.now());
        customer.setDeletedAt(null);
        customer.setIsActive(true);
        meterRegistry.counter("customer_enabled").increment();

        customerRepository.persist(customer);

        return customer;
    }


}
