package br.com.matteusmoreno.garage_manager.customer.controller;

import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.customer.request.CreateCustomerRequest;
import br.com.matteusmoreno.garage_manager.customer.request.UpdateCustomerRequest;
import br.com.matteusmoreno.garage_manager.customer.response.CustomerDetailsResponse;
import br.com.matteusmoreno.garage_manager.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.UUID;

@Path("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    @Path("/create")
    public Response create(@Valid CreateCustomerRequest request, @Context UriInfo uriInfo) {
        Customer customer = customerService.createCustomer(request);
        URI uri = uriInfo.getAbsolutePathBuilder().build();

        return Response.created(uri).entity(new CustomerDetailsResponse(customer)).build();
    }

    @GET
    @Path("/find-by-id/{id}")
    public Response findById(UUID id) {
        Customer customer = customerService.findCustomerById(id);

        return Response.ok(new CustomerDetailsResponse(customer)).build();
    }

    @PUT
    @Path("/update")
    public Response update(@Valid UpdateCustomerRequest request) {
        Customer customer = customerService.updateCustomer(request);

        return Response.ok(new CustomerDetailsResponse(customer)).build();
    }

    @DELETE
    @Path("/disable/{id}")
    public Response disable(UUID id) {
        customerService.disableCustomerById(id);

        return Response.noContent().build();
    }

    @PATCH
    @Path("/enable/{id}")
    public Response enable(UUID id) {
        Customer customer = customerService.enableCustomerById(id);

        return Response.ok(new CustomerDetailsResponse(customer)).build();
    }
}
