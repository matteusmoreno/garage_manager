package br.com.matteusmoreno.garage_manager.service_order.controller;

import br.com.matteusmoreno.garage_manager.service_order.request.UpdateServiceOrderRequest;
import br.com.matteusmoreno.garage_manager.service_order.response.ServiceOrderDetailsResponse;
import br.com.matteusmoreno.garage_manager.service_order.service.ServiceOrderService;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import br.com.matteusmoreno.garage_manager.service_order.request.CreateServiceOrderRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @Path("/create")
    @POST
    public Response create(@Valid CreateServiceOrderRequest request, @Context UriInfo uriInfo) {
        ServiceOrder serviceOrder = serviceOrderService.createServiceOrder(request);
        URI uri = uriInfo.getAbsolutePathBuilder().path(serviceOrder.getId().toString()).build();

        return Response.created(uri).entity(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }

    @Path("/find-by-id/{id}")
    @GET
    public Response findById(Long id) {
        ServiceOrder serviceOrder = serviceOrderService.findServiceOrderById(id);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }

    @Path("/update")
    @PUT
    public Response update(@Valid UpdateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderService.updateServiceOrder(request);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }
}
