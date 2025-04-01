package br.com.matteusmoreno.garage_manager.employee.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.address.service.AddressService;
import br.com.matteusmoreno.garage_manager.employee.constant.EmployeeRole;
import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.exception.exception_class.EmployeeNotFoundException;
import br.com.matteusmoreno.garage_manager.utils.UtilsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Employee Service Tests")
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    UtilsService utilsService;

    @Mock
    AddressService addressService;

    @Mock
    MeterRegistry meterRegistry;

    @InjectMocks
    EmployeeService employeeService;

    private CreateEmployeeRequest createEmployeeRequest;
    private Address address;
    private Employee employee;

    @BeforeEach
    void setUp() {
        createEmployeeRequest = new CreateEmployeeRequest("username", "password", "name", "email", "phone", "birthDate", "cpf", EmployeeRole.MECHANIC,"28994-666", "123", "Complement");
        address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        employee = new Employee(UUID.randomUUID(), "username", "password", "name", "email", "phone", "birthDate", 20, "cpf", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);

        setupMeterRegistry();
    }

    @Test
    @DisplayName("Should create a new employee correctly")
    void shouldCreateEmployee() {
        when(utilsService.cpfValidation(createEmployeeRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(createEmployeeRequest.birthDate())).thenReturn(true);
        when(employeeRepository.existsByCpfOrEmail(createEmployeeRequest.cpf(), createEmployeeRequest.email())).thenReturn(false);
        when(employeeRepository.existsByUsername(createEmployeeRequest.username())).thenReturn(false);
        when(utilsService.calculateAge(anyString())).thenReturn(20);
        when(addressService.createAddress(createEmployeeRequest.zipCode(), createEmployeeRequest.addressNumber(), createEmployeeRequest.addressComplement())).thenReturn(address);

        Employee result = employeeService.createEmployee(createEmployeeRequest);

        verify(utilsService, times(1)).cpfValidation(createEmployeeRequest.cpf());
        verify(utilsService, times(1)).cpfValidation(createEmployeeRequest.cpf());
        verify(employeeRepository, times(1)).existsByUsername(createEmployeeRequest.username());
        verify(employeeRepository, times(1)).existsByCpfOrEmail(createEmployeeRequest.cpf(), createEmployeeRequest.email());
        verify(utilsService, times(1)).calculateAge(createEmployeeRequest.birthDate());
        verify(addressService, times(1)).createAddress(address.getZipCode(), address.getNumber(), address.getComplement());
        verify(employeeRepository, times(1)).persist(result);

        assertAll(
                () -> assertEquals(createEmployeeRequest.username(), result.getUsername()),
                () -> assertEquals(createEmployeeRequest.password(), result.getPassword()),
                () -> assertEquals(createEmployeeRequest.name(), result.getName()),
                () -> assertEquals(createEmployeeRequest.email(), result.getEmail()),
                () -> assertEquals(createEmployeeRequest.phone(), result.getPhone()),
                () -> assertEquals(createEmployeeRequest.birthDate(), result.getBirthDate()),
                () -> assertEquals(20, result.getAge()),
                () -> assertEquals(createEmployeeRequest.cpf(), result.getCpf()),
                () -> assertEquals(createEmployeeRequest.role(), result.getRole()),
                () -> assertEquals(address, result.getAddress()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())

        );
    }

    @Test
    @DisplayName("Should find an employee by ID correctly")
    void shouldFindEmployeeByIdCorrectly() {
        when(employeeRepository.findByUUID(employee.getId())).thenReturn(employee);

        Employee result = employeeService.findEmployeeById(employee.getId());

        verify(employeeRepository, times(2)).findByUUID(employee.getId());

        assertEquals(employee, result);
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when employee not found")
    void shouldThrowEmployeeNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(employeeRepository.findByUUID(nonExistentId)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findEmployeeById(nonExistentId));

        verify(employeeRepository, times(1)).findByUUID(nonExistentId);
    }

    private void setupMeterRegistry() {
        Counter counter = mock(Counter.class);
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        doNothing().when(counter).increment();
    }

}