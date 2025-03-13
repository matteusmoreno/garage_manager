package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.CustomerAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.ropository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final MeterRegistry meterRegistry;

    public CustomerService(CustomerRepository customerRepository, AddressService addressService, MeterRegistry meterRegistry) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByCpfOrEmail(request.cpf(), request.email())) {
            meterRegistry.counter("customer_exists").increment();
            throw new CustomerAlreadyExistsException("Customer already exists with the provided CPF or email");
        }

        Address address = addressService.createAddress(request.zipCode(), request.addressNumber(), request.addressComplement());
        Integer age = calculateAge(request.birthDate());

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

        meterRegistry.counter("customer_created").increment();

        customerRepository.persist(customer);

        return customer;
    }

    private Integer calculateAge(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
        LocalDate currentDate = LocalDate.now();

        return currentDate.getYear() - birthDate.getYear();
    }
}
