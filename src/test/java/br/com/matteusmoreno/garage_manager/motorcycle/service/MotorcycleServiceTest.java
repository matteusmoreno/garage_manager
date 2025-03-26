package br.com.matteusmoreno.garage_manager.motorcycle.service;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.motorcycle.request.CreateMotorcycleRequest;
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

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        customer = new Customer(UUID.randomUUID(), "Fábio", "15/12/1990", 31, "(22)99999-9999", "fabio@email.com", "130.320.690-09", address, new ArrayList<>(), LocalDateTime.of(2025, 1, 1, 0, 0), null, null, true);
        createMotorcycleRequest = new CreateMotorcycleRequest(MotorcycleBrand.HONDA, "BIZ 125cc", "2002", "AZUL", "ABC1234");

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
        when(motorcycleRepository.findByLicensePlate(anyString())).thenReturn(true);

        List<CreateMotorcycleRequest> requestsList = List.of(createMotorcycleRequest);

        assertThrows(MotorcycleAlreadyExistsException.class, () -> motorcycleService.createMotorcycle(requestsList, customer));

        verify(motorcycleRepository, times(1)).findByLicensePlate(requestsList.getFirst().licensePlate());
    }

    private void setupMeterRegistry() {
        Counter counter = mock(Counter.class);
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        doNothing().when(counter).increment();
    }
}