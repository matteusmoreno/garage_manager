package br.com.matteusmoreno.garage_manager.employee.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.address.service.AddressService;
import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.exception.exception_class.*;
import br.com.matteusmoreno.garage_manager.utils.UtilsService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UtilsService utilsService;
    private final AddressService addressService;
    private final MeterRegistry meterRegistry;

    public EmployeeService(EmployeeRepository employeeRepository, UtilsService utilsService, AddressService addressService, MeterRegistry meterRegistry) {
        this.employeeRepository = employeeRepository;
        this.utilsService = utilsService;
        this.addressService = addressService;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeRequest request) {
        if (!utilsService.cpfValidation(request.cpf())) {
            throw new CpfInvalidException("Invalid CPF");
        }

        if (!utilsService.dateValidation(request.birthDate())) {
            throw new InvalidDateException("Invalid date");
        }

        if (employeeRepository.existsByCpfOrEmail(request.cpf(), request.email())) {
            throw new EmployeeAlreadyExistsException("Employee already exists with the provided CPF or email");
        }

        if (employeeRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        Integer age = utilsService.calculateAge(request.birthDate());
        Address address = addressService.createAddress(request.zipCode(), request.addressNumber(), request.addressComplement());

        Employee employee = Employee.builder()
                .username(request.username())
                .password(request.password())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .birthDate(request.birthDate())
                .age(age)
                .cpf(request.cpf())
                .role(request.role())
                .address(address)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)
                .build();

        meterRegistry.counter("employee_created").increment();
        employeeRepository.persist(employee);

        return employee;
    }

    @Transactional
    public Employee findEmployeeById(UUID id) {
        if (employeeRepository.findByUUID(id) == null) {
            meterRegistry.counter("employee_not_found").increment();
            throw new EmployeeNotFoundException("Employee not found");
        }

        Employee employee = employeeRepository.findByUUID(id);
        meterRegistry.counter("employee_found_by_id").increment();

        return employee;
    }
}
