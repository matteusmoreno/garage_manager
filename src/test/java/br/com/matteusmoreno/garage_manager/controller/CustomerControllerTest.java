package br.com.matteusmoreno.garage_manager.controller;

import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.request.UpdateCustomerRequest;
import br.com.matteusmoreno.garage_manager.response.CustomerDetailsResponse;
import br.com.matteusmoreno.garage_manager.service.CustomerService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Controller Tests")
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    UriInfo uriInfo;

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private CreateCustomerRequest createCustomerRequest;
    private Address address;
    private Customer customer;
    private CustomerDetailsResponse customerDetailsResponse;
    private UpdateCustomerRequest updateCustomerRequest;
    private Customer updatedCustomer;

    @BeforeEach
    void setUp() {
        createCustomerRequest = new CreateCustomerRequest("Customer Test", "28/08/1990", "(22)99822-3307", "customer@email.com", "034.527.990-50", "28994-675", "223", "Armação Motos", new ArrayList<>());
        address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        customer = new Customer(UUID.randomUUID(), "Customer Test", "28/08/1990", 34, "(22)99822-3307", "customer@email.com", "034.527.990-50", address, new ArrayList<>(), LocalDateTime.now(), null, null, true);
        customerDetailsResponse = new CustomerDetailsResponse(customer);
        updateCustomerRequest = new UpdateCustomerRequest(UUID.randomUUID(), "Updated Customer Test", "30/08/1990", "(22)00000-0000", "newemail@email.com", "309.609.840-97", "", "", "");
        updatedCustomer = new Customer(UUID.randomUUID(), "Updated Customer Test", "30/08/1990", 31, "(22)00000-0000", "(22)00000-0000", "309.609.840-97", address, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now(), null, true);
    }

    @Test
    @DisplayName("Should create a customer")
    void shouldCreateCustomer() {
        URI uri = URI.create("http://localhost:8080/customers/create/1");

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(UriBuilder.fromUri(uri));
        when(customerService.createCustomer(createCustomerRequest)).thenReturn(customer);

        Response response = customerController.create(createCustomerRequest, uriInfo);

        verify(customerService, times(1)).createCustomer(createCustomerRequest);

        assertAll(
                () -> assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus()),
                () -> assertEquals(uri, response.getLocation())
        );
    }

    @Test
    @DisplayName("Should find a customer by id")
    void shouldFindCustomerById() {
        UUID id = UUID.randomUUID();

        when(customerService.findCustomerById(id)).thenReturn(customer);

        Response response = customerController.findById(id);

        verify(customerService, times(1)).findCustomerById(id);

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(customer.getId(), customerDetailsResponse.id()),
                () -> assertEquals(customer.getName(), customerDetailsResponse.name()),
                () -> assertEquals(customer.getBirthDate(), customerDetailsResponse.birthDate()),
                () -> assertEquals(customer.getAge(), customerDetailsResponse.age()),
                () -> assertEquals(customer.getPhone(), customerDetailsResponse.phone()),
                () -> assertEquals(customer.getEmail(), customerDetailsResponse.email()),
                () -> assertEquals(customer.getCpf(), customerDetailsResponse.cpf()),
                () -> assertEquals(customer.getAddress(), address),
                () -> assertEquals(customer.getCreatedAt(), customerDetailsResponse.createdAt()),
                () -> assertEquals(customer.getUpdatedAt(), customerDetailsResponse.updatedAt()),
                () -> assertEquals(customer.getDeletedAt(), customerDetailsResponse.deletedAt()),
                () -> assertEquals(customer.getIsActive(), customerDetailsResponse.isActive())
        );
    }

    @Test
    @DisplayName("Should update a customer")
    void shouldUpdateCustomer() {
        when(customerService.updateCustomer(updateCustomerRequest)).thenReturn(updatedCustomer);

        Response response = customerController.update(updateCustomerRequest);
        CustomerDetailsResponse responseBody = new CustomerDetailsResponse(updatedCustomer);


        verify(customerService, times(1)).updateCustomer(updateCustomerRequest);

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(updatedCustomer.getId(), responseBody.id()),
                () -> assertEquals(updatedCustomer.getName(), responseBody.name()),
                () -> assertEquals(updatedCustomer.getBirthDate(), responseBody.birthDate()),
                () -> assertEquals(updatedCustomer.getAge(), responseBody.age()),
                () -> assertEquals(updatedCustomer.getPhone(), responseBody.phone()),
                () -> assertEquals(updatedCustomer.getEmail(), responseBody.email()),
                () -> assertEquals(updatedCustomer.getCpf(), responseBody.cpf()),
                () -> assertEquals(updatedCustomer.getAddress(), address),
                () -> assertEquals(updatedCustomer.getCreatedAt(), responseBody.createdAt()),
                () -> assertEquals(updatedCustomer.getUpdatedAt(), responseBody.updatedAt()),
                () -> assertEquals(updatedCustomer.getDeletedAt(), responseBody.deletedAt()),
                () -> assertEquals(updatedCustomer.getIsActive(), responseBody.isActive())
        );
    }

    @Test
    @DisplayName("Should disable a customer")
    void shouldDisableCustomer() {
        doNothing().when(customerService).disableCustomerById(customer.getId());
        Response response = customerController.disable(customer.getId());

        verify(customerService).disableCustomerById(customer.getId());
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Should enable a customer")
    void shouldEnableCustomer() {
        customer.setDeletedAt(LocalDateTime.now());
        customer.setIsActive(false);

        when(customerService.enableCustomerById(customer.getId())).thenReturn(customer);

        Response response = customerController.enable(customer.getId());

        verify(customerService).enableCustomerById(customer.getId());

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(customer.getId(), customerDetailsResponse.id()),
                () -> assertEquals(customer.getName(), customerDetailsResponse.name()),
                () -> assertEquals(customer.getBirthDate(), customerDetailsResponse.birthDate()),
                () -> assertEquals(customer.getAge(), customerDetailsResponse.age()),
                () -> assertEquals(customer.getPhone(), customerDetailsResponse.phone()),
                () -> assertEquals(customer.getEmail(), customerDetailsResponse.email()),
                () -> assertEquals(customer.getCpf(), customerDetailsResponse.cpf()),
                () -> assertEquals(customer.getAddress(), address),
                () -> assertEquals(customer.getCreatedAt(), customerDetailsResponse.createdAt()),
                () -> assertEquals(customer.getUpdatedAt(), customerDetailsResponse.updatedAt()),
                () -> assertNull(customerDetailsResponse.deletedAt()),
                () -> assertTrue(customerDetailsResponse.isActive())
        );
    }
}