package br.com.matteusmoreno.garage_manager.employee.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.address.service.AddressService;
import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.request.UpdateEmployeeRequest;
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

    @Transactional
    public Employee findEmployeeByUsername(String username) {
        if (employeeRepository.findByUsername(username) == null) {
            meterRegistry.counter("employee_not_found").increment();
            throw new EmployeeNotFoundException("Employee not found");
        }

        Employee employee = employeeRepository.findByUsername(username);
        meterRegistry.counter("employee_found_by_username").increment();

        return employee;
    }

    @Transactional
    public Employee updateEmployee(UpdateEmployeeRequest request) {
        Employee employee = findEmployeeById(request.id());

        if (request.username() != null) employee.setUsername(request.username());
        if (request.password() != null) employee.setPassword(request.password());
        if (request.name() != null) employee.setName(request.name());
        if (request.email() != null) employee.setEmail(request.email());
        if (request.phone() != null) employee.setPhone(request.phone());
        if (request.birthDate() != null) {
            if (!utilsService.dateValidation(request.birthDate())) {
                throw new InvalidDateException("Invalid date");
            }
            employee.setBirthDate(request.birthDate());
            employee.setAge(utilsService.calculateAge(request.birthDate()));
        }
        if (request.cpf() != null) {
            if (!utilsService.cpfValidation(request.cpf())) {
                throw new CpfInvalidException("Invalid CPF");
            }
            employee.setCpf(request.cpf());
        }
        if (request.role() != null) employee.setRole(request.role());
        if (request.zipCode() != null) {
            Address address = addressService.createAddress(request.zipCode(), request.addressNumber(), request.addressComplement());
            employee.setAddress(address);
        }

        employee.setUpdatedAt(LocalDateTime.now());

        meterRegistry.counter("employee_updated").increment();
        employeeRepository.persist(employee);

        return employee;
    }

    @Transactional
    public void disableEmployeeById(UUID id) {
        Employee employee = findEmployeeById(id);

        if (!employee.getIsActive()) {
            meterRegistry.counter("employee_already_disabled").increment();
            throw new EmployeeIsAlreadyDisabledException("Employee is already disabled");
        }

        employee.setIsActive(false);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.persist(employee);
    }

    @Transactional
    public Employee enableEmployeeById(UUID id) {
        Employee employee = findEmployeeById(id);

        if (employee.getIsActive()) {
            meterRegistry.counter("employee_already_enabled").increment();
            throw new EmployeeIsAlreadyEnabledException("Employee is already enabled");
        }

        employee.setIsActive(true);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeletedAt(null);
        employeeRepository.persist(employee);

        return employee;
    }
}
