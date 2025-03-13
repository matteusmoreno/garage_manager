package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.CpfInvalidException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.CustomerAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.InvalidDateException;
import br.com.matteusmoreno.garage_manager.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.ropository.CustomerRepository;
import br.com.safeguard.check.SafeguardCheck;
import br.com.safeguard.interfaces.Check;
import br.com.safeguard.types.ParametroTipo;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

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

    private Integer calculateAge(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
        LocalDate currentDate = LocalDate.now();

        return currentDate.getYear() - birthDate.getYear();
    }

    private Boolean cpfValidation(String cpf) {
        Check check = new SafeguardCheck();
        return !check.elementOf(cpf, ParametroTipo.CPF).validate().hasError();
    }


    private Boolean dateValidation(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT)
                .withLocale(Locale.getDefault());
        try {
            LocalDate.parse(birthDateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
