package br.com.matteusmoreno.garage_manager.employee.controller;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.request.UpdateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.response.EmployeeDetailsResponse;
import br.com.matteusmoreno.garage_manager.employee.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.net.URI;
import java.util.UUID;
@SecurityRequirement(name = "SecurityScheme")
@Path("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Path("/create")
    @POST
    public Response create(@Valid CreateEmployeeRequest request, @Context UriInfo uriInfo) {
        Employee employee = employeeService.createEmployee(request);
        URI uri = uriInfo.getAbsolutePathBuilder().path(employee.getId().toString()).build();
        return Response.created(uri).entity(new EmployeeDetailsResponse(employee)).build();
    }

    @Path("/find-by-id/{id}")
    @GET
    public Response findById(UUID id) {
        Employee employee = employeeService.findEmployeeById(id);
        return Response.ok(new EmployeeDetailsResponse(employee)).build();
    }

    @Path("/find-by-username/{username}")
    @GET
    public Response findByUsername(String username) {
        Employee employee = employeeService.findEmployeeByUsername(username);
        return Response.ok(new EmployeeDetailsResponse(employee)).build();
    }

    @Path("/update")
    @PUT
    public Response update(@Valid UpdateEmployeeRequest request) {
        Employee employee = employeeService.updateEmployee(request);
        return Response.ok(new EmployeeDetailsResponse(employee)).build();
    }

    @Path("/disable/{id}")
    @DELETE
    public Response disable(UUID id) {
        employeeService.disableEmployeeById(id);
        return Response.noContent().build();
    }

    @Path("/enable/{id}")
    @PATCH
    public Response enable(UUID id) {
        Employee employee = employeeService.enableEmployeeById(id);
        return Response.ok(new EmployeeDetailsResponse(employee)).build();
    }
}
