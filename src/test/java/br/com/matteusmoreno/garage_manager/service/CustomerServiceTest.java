package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.*;
import br.com.matteusmoreno.garage_manager.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.request.UpdateCustomerRequest;
import br.com.matteusmoreno.garage_manager.ropository.CustomerRepository;
import br.com.matteusmoreno.garage_manager.utils.UtilsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    MeterRegistry meterRegistry;

    @Mock
    AddressService addressService;

    @Mock
    UtilsService utilsService;

    @InjectMocks
    CustomerService customerService;

    private CreateCustomerRequest createCustomerRequest;
    private Address address;
    private Address newAddress;
    private Customer customer;
    private UpdateCustomerRequest updateCustomerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createCustomerRequest = new CreateCustomerRequest("Name", "28/08/1990", "(22)99822-3307", "email@email.com", "134.782.410-30", "28994-666", "123", "complement");
        address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        customer = new Customer(UUID.randomUUID(), "Fábio", "15/12/1990", 31, "(22)99999-9999", "fabio@email.com", "130.320.690-09", address, LocalDateTime.of(2025, 1, 1, 0, 0), null, null, true);
        updateCustomerRequest = new UpdateCustomerRequest(customer.getId(),"Fabio Silva", "15/12/1970", "(22)00000-0000", "fabio@newemail.com", "067.028.380-00", "21380-310", "456", "new complement");
        newAddress = new Address(2L, "21380-310", "New Street", "New Neighborhood", "456", "New City", "New State", "New Complement");

        setupMeterRegistry();
    }

    @Test
    @DisplayName("Should create customer correctly")
    void shouldCreateCustomerCorrectly() {
        when(utilsService.cpfValidation(createCustomerRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(createCustomerRequest.birthDate())).thenReturn(true);
        when(customerRepository.existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email())).thenReturn(false);
        when(addressService.createAddress(createCustomerRequest.zipCode(), createCustomerRequest.addressNumber(), createCustomerRequest.addressComplement())).thenReturn(address);

        Customer response = customerService.createCustomer(createCustomerRequest);

        verify(utilsService, times(1)).cpfValidation(createCustomerRequest.cpf());
        verify(utilsService, times(1)).dateValidation(createCustomerRequest.birthDate());
        verify(customerRepository, times(1)).existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email());
        verify(addressService, times(1)).createAddress(createCustomerRequest.zipCode(), createCustomerRequest.addressNumber(), createCustomerRequest.addressComplement());
        verify(utilsService, times(1)).calculateAge(createCustomerRequest.birthDate());
        verify(customerRepository, times(1)).persist(response);

        assertAll(
                () -> assertEquals(createCustomerRequest.name(), response.getName()),
                () -> assertEquals(createCustomerRequest.birthDate(), response.getBirthDate()),
                () -> assertNotNull(response.getAge()),
                () -> assertEquals(createCustomerRequest.phone(), response.getPhone()),
                () -> assertEquals(createCustomerRequest.email(), response.getEmail()),
                () -> assertEquals(createCustomerRequest.cpf(), response.getCpf()),
                () -> assertEquals(address, response.getAddress()),
                () -> assertNotNull(response.getCreatedAt()),
                () -> assertNull(response.getUpdatedAt()),
                () -> assertNull(response.getDeletedAt()),
                () -> assertTrue(response.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw CpfInvalidException when CPF is invalid")
    void shouldThrowCpfInvalidExceptionWhenCPFIsInvalid() {
        when(utilsService.cpfValidation(createCustomerRequest.cpf())).thenReturn(false);

        assertThrows(CpfInvalidException.class, () -> customerService.createCustomer(createCustomerRequest));

        verify(utilsService, times(1)).cpfValidation(createCustomerRequest.cpf());
        verify(utilsService, never()).dateValidation(createCustomerRequest.birthDate());
        verify(customerRepository, never()).existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email());
        verify(addressService, never()).createAddress(createCustomerRequest.zipCode(), createCustomerRequest.addressNumber(), createCustomerRequest.addressComplement());
        verify(utilsService, never()).calculateAge(createCustomerRequest.birthDate());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw InvalidDateException when date is invalid")
    void shouldThrowInvalidDateExceptionWhenDateIsInvalid() {
        when(utilsService.cpfValidation(createCustomerRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(createCustomerRequest.birthDate())).thenReturn(false);

        assertThrows(InvalidDateException.class, () -> customerService.createCustomer(createCustomerRequest));

        verify(utilsService, times(1)).cpfValidation(createCustomerRequest.cpf());
        verify(utilsService, times(1)).dateValidation(createCustomerRequest.birthDate());
        verify(customerRepository, never()).existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email());
        verify(addressService, never()).createAddress(createCustomerRequest.zipCode(), createCustomerRequest.addressNumber(), createCustomerRequest.addressComplement());
        verify(utilsService, never()).calculateAge(createCustomerRequest.birthDate());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw CustomerAlreadyExistsException when customer already exists")
    void shouldThrowCustomerAlreadyExistsExceptionWhenCustomerAlreadyExists() {
        when(utilsService.cpfValidation(createCustomerRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(createCustomerRequest.birthDate())).thenReturn(true);
        when(customerRepository.existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.createCustomer(createCustomerRequest));

        verify(utilsService, times(1)).cpfValidation(createCustomerRequest.cpf());
        verify(utilsService, times(1)).dateValidation(createCustomerRequest.birthDate());
        verify(customerRepository, times(1)).existsByCpfOrEmail(createCustomerRequest.cpf(), createCustomerRequest.email());
        verify(addressService, never()).createAddress(createCustomerRequest.zipCode(), createCustomerRequest.addressNumber(), createCustomerRequest.addressComplement());
        verify(utilsService, never()).calculateAge(createCustomerRequest.birthDate());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    @DisplayName("Should find customer by id correctly")
    void shouldFindCustomerByIdCorrectly() {
        when(customerRepository.findByUUID(customer.getId())).thenReturn(customer);

        Customer response = customerService.findCustomerById(customer.getId());

        verify(customerRepository, times(2)).findByUUID(customer.getId());

        assertAll(
                () -> assertEquals(customer, response),
                () -> assertEquals(customer.getName(), response.getName()),
                () -> assertEquals(customer.getBirthDate(), response.getBirthDate()),
                () -> assertEquals(customer.getAge(), response.getAge()),
                () -> assertEquals(customer.getPhone(), response.getPhone()),
                () -> assertEquals(customer.getEmail(), response.getEmail()),
                () -> assertEquals(customer.getCpf(), response.getCpf()),
                () -> assertEquals(customer.getAddress(), response.getAddress()),
                () -> assertEquals(customer.getCreatedAt(), response.getCreatedAt()),
                () -> assertEquals(customer.getUpdatedAt(), response.getUpdatedAt()),
                () -> assertEquals(customer.getDeletedAt(), response.getDeletedAt()),
                () -> assertEquals(customer.getIsActive(), response.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found")
    void shouldThrowCustomerNotFoundExceptionWhenCustomerNotFound() {
        when(customerRepository.findByUUID(customer.getId())).thenReturn(null);

        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(customer.getId()));

        verify(customerRepository, times(1)).findByUUID(customer.getId());
    }

    @Test
    @DisplayName("Should update customer correctly")
    void shouldUpdateCustomerCorrectly() {
        when(customerRepository.findByUUID(updateCustomerRequest.id())).thenReturn(customer);
        when(utilsService.cpfValidation(updateCustomerRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(updateCustomerRequest.birthDate())).thenReturn(true);
        when(utilsService.calculateAge(updateCustomerRequest.birthDate())).thenReturn(50);
        when(addressService.createAddress(updateCustomerRequest.zipCode(), updateCustomerRequest.addressNumber(), updateCustomerRequest.addressComplement())).thenReturn(newAddress);

        Customer response = customerService.updateCustomer(updateCustomerRequest);

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(utilsService, times(1)).cpfValidation(updateCustomerRequest.cpf());
        verify(utilsService, times(1)).dateValidation(updateCustomerRequest.birthDate());
        verify(utilsService, times(1)).calculateAge(updateCustomerRequest.birthDate());
        verify(addressService, times(1)).createAddress(updateCustomerRequest.zipCode(), updateCustomerRequest.addressNumber(), updateCustomerRequest.addressComplement());
        verify(customerRepository, times(1)).persist(response);

        assertAll(
                () -> assertEquals(updateCustomerRequest.id(), customer.getId()),
                () -> assertEquals(customer, response),
                () -> assertEquals(updateCustomerRequest.name(), response.getName()),
                () -> assertEquals(updateCustomerRequest.birthDate(), response.getBirthDate()),
                () -> assertEquals(50, response.getAge()),
                () -> assertEquals(updateCustomerRequest.phone(), response.getPhone()),
                () -> assertEquals(updateCustomerRequest.email(), response.getEmail()),
                () -> assertEquals(updateCustomerRequest.cpf(), response.getCpf()),
                () -> assertEquals(newAddress, response.getAddress()),
                () -> assertNotNull(response.getCreatedAt()),
                () -> assertNotNull(response.getUpdatedAt()),
                () -> assertNull(response.getDeletedAt()),
                () -> assertTrue(response.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw CpfInvalidException when CPF is invalid")
    void shouldThrowCpfInvalidExceptionWhenCPFIsInvalidOnUpdate() {
        when(customerRepository.findByUUID(updateCustomerRequest.id())).thenReturn(customer);
        when(utilsService.cpfValidation(updateCustomerRequest.cpf())).thenReturn(false);

        assertThrows(CpfInvalidException.class, () -> customerService.updateCustomer(updateCustomerRequest));

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(utilsService, times(1)).cpfValidation(updateCustomerRequest.cpf());
        verify(utilsService, never()).dateValidation(updateCustomerRequest.birthDate());
        verify(utilsService, never()).calculateAge(updateCustomerRequest.birthDate());
        verify(addressService, never()).createAddress(updateCustomerRequest.zipCode(), updateCustomerRequest.addressNumber(), updateCustomerRequest.addressComplement());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw InvalidDateException when date is invalid")
    void shouldThrowInvalidDateExceptionWhenDateIsInvalidOnUpdate() {
        when(customerRepository.findByUUID(updateCustomerRequest.id())).thenReturn(customer);
        when(utilsService.cpfValidation(updateCustomerRequest.cpf())).thenReturn(true);
        when(utilsService.dateValidation(updateCustomerRequest.birthDate())).thenReturn(false);

        assertThrows(InvalidDateException.class, () -> customerService.updateCustomer(updateCustomerRequest));

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(utilsService, times(1)).cpfValidation(updateCustomerRequest.cpf());
        verify(utilsService, times(1)).dateValidation(updateCustomerRequest.birthDate());
        verify(utilsService, never()).calculateAge(updateCustomerRequest.birthDate());
        verify(addressService, never()).createAddress(updateCustomerRequest.zipCode(), updateCustomerRequest.addressNumber(), updateCustomerRequest.addressComplement());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    @DisplayName("Should disable customer correctly")
    void shouldDisableCustomerCorrectly() {
        customer.setIsActive(true);
        when(customerRepository.findByUUID(customer.getId())).thenReturn(customer);

        customerService.disableCustomerById(customer.getId());

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(customerRepository, times(1)).persist(customer);

        assertFalse(customer.getIsActive());
    }

    @Test
    @DisplayName("Should throw CustomerIsAlreadyDisabledException when customer is already disabled")
    void shouldThrowCustomerIsAlreadyDisabledExceptionWhenCustomerIsAlreadyDisabled() {
        customer.setIsActive(false);
        customer.setDeletedAt(null);
        when(customerRepository.findByUUID(customer.getId())).thenReturn(customer);

        assertThrows(CustomerIsAlreadyDisabledException.class, () -> customerService.disableCustomerById(customer.getId()));

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(customerRepository, never()).persist(customer);
    }

    @Test
    @DisplayName("Should enable customer correctly")
    void shouldEnableCustomerCorrectly() {
        customer.setIsActive(false);
        customer.setDeletedAt(LocalDateTime.now());
        when(customerRepository.findByUUID(customer.getId())).thenReturn(customer);

        customerService.enableCustomerById(customer.getId());

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(customerRepository, times(1)).persist(customer);

        assertTrue(customer.getIsActive());
        assertNull(customer.getDeletedAt());
    }

    @Test
    @DisplayName("Should throw CustomerIsAlreadyEnabledException when customer is already enabled")
    void shouldThrowCustomerIsAlreadyEnabledExceptionWhenCustomerIsAlreadyEnabled() {
        customer.setIsActive(true);
        when(customerRepository.findByUUID(customer.getId())).thenReturn(customer);

        assertThrows(CustomerIsAlreadyEnabledException.class, () -> customerService.enableCustomerById(customer.getId()));

        verify(customerRepository, times(2)).findByUUID(customer.getId());
        verify(customerRepository, never()).persist(customer);
    }

    private void setupMeterRegistry() {
        Counter counter = mock(Counter.class);
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        doNothing().when(counter).increment();
    }
}
