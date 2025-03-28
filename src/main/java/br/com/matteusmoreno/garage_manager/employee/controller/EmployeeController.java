package br.com.matteusmoreno.garage_manager.employee.controller;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.request.CreateEmployeeRequest;
import br.com.matteusmoreno.garage_manager.employee.response.EmployeeDetailsResponse;
import br.com.matteusmoreno.garage_manager.employee.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

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
}
