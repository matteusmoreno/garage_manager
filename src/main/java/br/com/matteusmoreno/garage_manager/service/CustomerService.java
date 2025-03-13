package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.CpfInvalidException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.CustomerAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.InvalidDateException;
import br.com.matteusmoreno.garage_manager.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.ropository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import static br.com.matteusmoreno.garage_manager.utils.UtilsService.*;

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
        if (!cpfValidation(request.cpf())) {
            throw new CpfInvalidException("Invalid CPF");
        }

        if (!dateValidation(request.birthDate())) {
            throw new InvalidDateException("Invalid date");
        }

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

}
