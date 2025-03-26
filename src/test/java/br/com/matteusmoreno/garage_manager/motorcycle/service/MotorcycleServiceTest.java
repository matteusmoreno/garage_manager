package br.com.matteusmoreno.garage_manager.motorcycle.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleIsAlreadyDisabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleNotFoundException;
import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.motorcycle.request.CreateMotorcycleRequest;
import br.com.matteusmoreno.garage_manager.motorcycle.request.UpdateMotorcycleRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Motorcycle Service Tests")
@ExtendWith(MockitoExtension.class)
class MotorcycleServiceTest {

    @Mock
    MotorcycleRepository motorcycleRepository;

    @Mock
    MeterRegistry meterRegistry;

    @InjectMocks
    MotorcycleService motorcycleService;

    private CreateMotorcycleRequest createMotorcycleRequest;
    private Customer customer;
    private Motorcycle motorcycle;
    private UpdateMotorcycleRequest updateMotorcycleRequest;

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        customer = new Customer(UUID.randomUUID(), "Fábio", "15/12/1990", 31, "(22)99999-9999", "fabio@email.com", "130.320.690-09", address, new ArrayList<>(), LocalDateTime.of(2025, 1, 1, 0, 0), null, null, true);
        createMotorcycleRequest = new CreateMotorcycleRequest(MotorcycleBrand.HONDA, "BIZ 125cc", "2002", "AZUL", "ABC1234");
        motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "BIZ 125cc", "2002", "AZUL", "ABC1234", customer, LocalDateTime.of(2025, 1, 1, 0, 0), null, null, true);
        updateMotorcycleRequest = new UpdateMotorcycleRequest(motorcycle.getId(), MotorcycleBrand.YAMAHA, "YBR Factor 125cc", "2005", "PRETA", "XYZ9876");

        setupMeterRegistry();
    }

    @Test
    @DisplayName("Should create a motorcycle correctly")
    void shouldCreateMotorcycleCorrectly() {
        List<CreateMotorcycleRequest> requestsList = List.of(createMotorcycleRequest);

        List<Motorcycle> result = motorcycleService.createMotorcycle(requestsList, customer);

        verify(motorcycleRepository, times(1)).persist(result.getFirst());

        assertAll(
                () -> assertEquals(customer, result.getFirst().getCustomer()),
                () -> assertEquals(createMotorcycleRequest.brand(), result.getFirst().getBrand()),
                () -> assertEquals(createMotorcycleRequest.model(), result.getFirst().getModel()),
                () -> assertEquals(createMotorcycleRequest.year(), result.getFirst().getYear()),
                () -> assertEquals(createMotorcycleRequest.color(), result.getFirst().getColor()),
                () -> assertEquals(createMotorcycleRequest.licensePlate(), result.getFirst().getLicensePlate()),
                () -> assertNotNull(result.getFirst().getCreatedAt()),
                () -> assertNull(result.getFirst().getUpdatedAt()),
                () -> assertNull(result.getFirst().getDeletedAt()),
                () -> assertTrue(result.getFirst().getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw MotorcycleAlreadyExistsException when license plate already exists")
    void shouldThrowMotorcycleAlreadyExistsExceptionWhenLicensePlateAlreadyExists() {
        when(motorcycleRepository.existsByLicensePlate(anyString())).thenReturn(true);

        List<CreateMotorcycleRequest> requestsList = List.of(createMotorcycleRequest);

        assertThrows(MotorcycleAlreadyExistsException.class, () -> motorcycleService.createMotorcycle(requestsList, customer));

        verify(motorcycleRepository, times(1)).existsByLicensePlate(requestsList.getFirst().licensePlate());
    }

    @Test
    @DisplayName("Should find a motorcycle by id")
    void shouldFindMotorcycleById() {
        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(motorcycle);

        Motorcycle result = motorcycleService.findMotorcycleById(motorcycle.getId());

        verify(motorcycleRepository, times(2)).findByUUID(motorcycle.getId());

        assertEquals(motorcycle, result);
    }

    @Test
    @DisplayName("Should throw MotorcycleNotFoundException when motorcycle not found by id")
    void shouldThrowMotorcycleNotFoundExceptionWhenMotorcycleNotFoundById() {
        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(null);

        assertThrows(MotorcycleNotFoundException.class, () -> motorcycleService.findMotorcycleById(motorcycle.getId()));

        verify(motorcycleRepository, times(1)).findByUUID(motorcycle.getId());
    }

    @Test
    @DisplayName("Should find a motorcycle by license plate")
    void shouldFindMotorcycleByLicensePlate() {
        when(motorcycleRepository.findByLicensePlate(motorcycle.getLicensePlate())).thenReturn(motorcycle);

        Motorcycle result = motorcycleService.findMotorcycleByLicensePlate(motorcycle.getLicensePlate());

        verify(motorcycleRepository, times(2)).findByLicensePlate(motorcycle.getLicensePlate());

        assertEquals(motorcycle, result);
    }

    @Test
    @DisplayName("Should throw MotorcycleNotFoundException when motorcycle not found by license plate")
    void shouldThrowMotorcycleNotFoundExceptionWhenMotorcycleNotFoundByLicensePlate() {
        when(motorcycleRepository.findByLicensePlate(motorcycle.getLicensePlate())).thenReturn(null);

        assertThrows(MotorcycleNotFoundException.class, () -> motorcycleService.findMotorcycleByLicensePlate(motorcycle.getLicensePlate()));

        verify(motorcycleRepository, times(1)).findByLicensePlate(motorcycle.getLicensePlate());
    }

    @Test
    @DisplayName("Should update a motorcycle correctly")
    void shouldUpdateMotorcycleCorrectly() {
        when(motorcycleRepository.findByUUID(updateMotorcycleRequest.id())).thenReturn(motorcycle);

        Motorcycle result = motorcycleService.updateMotorcycle(updateMotorcycleRequest);

        verify(motorcycleRepository, times(2)).findByUUID(updateMotorcycleRequest.id());
        verify(motorcycleRepository, times(1)).persist(result);

        assertAll(
                () -> assertEquals(motorcycle.getId(), result.getId()),
                () -> assertEquals(updateMotorcycleRequest.brand(), result.getBrand()),
                () -> assertEquals(updateMotorcycleRequest.model(), result.getModel()),
                () -> assertEquals(updateMotorcycleRequest.year(), result.getYear()),
                () -> assertEquals(updateMotorcycleRequest.color(), result.getColor()),
                () -> assertEquals(updateMotorcycleRequest.licensePlate(), result.getLicensePlate()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNotNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @Test
    @DisplayName("Should disable a motorcycle correctly")
    void shouldDisableMotorcycleCorrectly() {
        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(motorcycle);

        motorcycleService.disableMotorcycle(motorcycle.getId());

        verify(motorcycleRepository, times(2)).findByUUID(motorcycle.getId());
        verify(motorcycleRepository, times(1)).persist(motorcycle);

        assertAll(
                () -> assertNotNull(motorcycle.getCreatedAt()),
                () -> assertNotNull(motorcycle.getDeletedAt()),
                () -> assertFalse(motorcycle.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw MotorcycleIsAlreadyDisabledException when motorcycle is already disabled")
    void shouldThrowMotorcycleIsAlreadyDisabledExceptionWhenMotorcycleIsAlreadyDisabled() {
        motorcycle.setIsActive(false);

        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(motorcycle);

        assertThrows(MotorcycleIsAlreadyDisabledException.class, () -> motorcycleService.disableMotorcycle(motorcycle.getId()));

        verify(motorcycleRepository, times(2)).findByUUID(motorcycle.getId());
    }

    @Test
    @DisplayName("Should enable a motorcycle correctly")
    void shouldEnableMotorcycleCorrectly() {
        motorcycle.setIsActive(false);

        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(motorcycle);

        Motorcycle result = motorcycleService.enableMotorcycle(motorcycle.getId());

        verify(motorcycleRepository, times(2)).findByUUID(motorcycle.getId());
        verify(motorcycleRepository, times(1)).persist(motorcycle);

        assertAll(
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNotNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw MotorcycleIsAlreadyEnabledException when motorcycle is already enabled")
    void shouldThrowMotorcycleIsAlreadyEnabledExceptionWhenMotorcycleIsAlreadyEnabled() {
        when(motorcycleRepository.findByUUID(motorcycle.getId())).thenReturn(motorcycle);

        assertThrows(MotorcycleIsAlreadyEnabledException.class, () -> motorcycleService.enableMotorcycle(motorcycle.getId()));

        verify(motorcycleRepository, times(2)).findByUUID(motorcycle.getId());
    }

    private void setupMeterRegistry() {
        Counter counter = mock(Counter.class);
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        doNothing().when(counter).increment();
    }
}