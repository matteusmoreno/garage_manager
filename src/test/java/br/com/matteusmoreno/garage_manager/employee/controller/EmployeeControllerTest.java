package br.com.matteusmoreno.garage_manager.employee.controller;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.employee.constant.EmployeeRole;
import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.request.UpdateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.response.EmployeeDetailsResponse;
import br.com.matteusmoreno.garage_manager.employee.service.EmployeeService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Employee Controller Tests")
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    UriInfo uriInfo;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController employeeController;

    private CreateEmployeeRequest createEmployeeRequest;
    private Employee employee;
    private EmployeeDetailsResponse employeeDetailsResponse;
    private UpdateEmployeeRequest updateEmployeeRequest;

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-666", "Street", "Neighborhood", "123", "City", "State", "Complement");
        createEmployeeRequest = new CreateEmployeeRequest("username", "password", "name", "email@email.com", "(22)99999-9999", "28/08/1990", "679.702.520-60", EmployeeRole.MECHANIC,"28994-666", "123", "Complement");
        employee = new Employee(UUID.randomUUID(), "username", "password", "name", "email@email.com", "(22)99999-9999", "28/08/1990", 44, "679.702.520-60", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        employeeDetailsResponse = new EmployeeDetailsResponse(employee);
        updateEmployeeRequest = new UpdateEmployeeRequest(employee.getId(), "newUsername", "newPassword", "newName", "newEmail", "newPhone", "newBirthDate", "280.487.090-15", EmployeeRole.MECHANIC, "28996-666", "", "");
    }

    @Test
    @DisplayName("Should create an employee")
    void shouldCreateAnEmployee() {
        URI uri = URI.create("http://localhost:8080/employees/create/"+employee.getId().toString());

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(UriBuilder.fromUri("http://localhost:8080/employees/create/"));
        when(employeeService.createEmployee(createEmployeeRequest)).thenReturn(employee);

        Response response = employeeController.create(createEmployeeRequest, uriInfo);

        verify(employeeService, times(1)).createEmployee(createEmployeeRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(uri, response.getLocation());
        assertEquals(employeeDetailsResponse, response.getEntity());
    }

    @Test
    @DisplayName("Should find an employee by ID")
    void shouldFindAnEmployeeById() {
        UUID id = employee.getId();

        when(employeeService.findEmployeeById(id)).thenReturn(employee);

        Response response = employeeController.findById(id);

        verify(employeeService, times(1)).findEmployeeById(id);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(employeeDetailsResponse, response.getEntity());
    }

    @Test
    @DisplayName("Should find an employee by username")
    void shouldFindAnEmployeeByUsername() {
        String username = employee.getUsername();

        when(employeeService.findEmployeeByUsername(username)).thenReturn(employee);

        Response response = employeeController.findByUsername(username);

        verify(employeeService, times(1)).findEmployeeByUsername(username);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(employeeDetailsResponse, response.getEntity());
    }

    @Test
    @DisplayName("Should update an employee")
    void shouldUpdateAnEmployee() {
        when(employeeService.updateEmployee(updateEmployeeRequest)).thenReturn(employee);

        Response response = employeeController.update(updateEmployeeRequest);

        verify(employeeService, times(1)).updateEmployee(updateEmployeeRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(employeeDetailsResponse, response.getEntity());
    }

    @Test
    @DisplayName("Should disable an employee")
    void shouldDisableAnEmployee() {
        UUID id = employee.getId();

        Response response = employeeController.disable(id);

        verify(employeeService, times(1)).disableEmployeeById(id);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Should enable an employee")
    void shouldEnableAnEmployee() {
        UUID id = employee.getId();

        Response response = employeeController.enable(id);

        verify(employeeService, times(1)).enableEmployeeById(id);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}