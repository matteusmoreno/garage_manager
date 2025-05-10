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
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SecurityRequirement(name = "SecurityScheme")
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

    @Path("/find-all")
    @GET
    public Response findAll() {
        List<ServiceOrderDetailsResponse> serviceOrders = serviceOrderService.findAllServiceOrder();
        return Response.ok(serviceOrders).build();
    }

    @Path("/find-by-date/{day}/{month}/{year}")
    @GET
    public Response findByDate(int day, int month, int year) {
        List<ServiceOrderDetailsResponse> serviceOrders = serviceOrderService.findServiceOrdersByDay(day, month, year);

        return Response.ok(serviceOrders).build();
    }

    @Path("/find-by-month/{month}/{year}")
    @GET
    public Response findByMonth(int month, int year) {
        List<ServiceOrderDetailsResponse> serviceOrders = serviceOrderService.findServiceOrdersByMonth(month, year);

        return Response.ok(serviceOrders).build();
    }

    @Path("/find-by-year/{year}")
    @GET
    public Response findByYear(int year) {
        List<ServiceOrderDetailsResponse> serviceOrders = serviceOrderService.findServiceOrdersByYear(year);

        return Response.ok(serviceOrders).build();
    }

    @Path("/update")
    @PUT
    public Response update(@Valid UpdateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderService.updateServiceOrder(request);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }

    @Path("/start/{id}")
    @PUT
    public Response startServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderService.startServiceOrder(id);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }

    @Path("/complete/{id}")
    @PUT
    public Response completeServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderService.completeServiceOrder(id);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }

    @Path("/cancel/{id}")
    @PUT
    public Response cancelServiceOrder(Long id) {
        ServiceOrder serviceOrder = serviceOrderService.cancelServiceOrder(id);
        return Response.ok(new ServiceOrderDetailsResponse(serviceOrder)).build();
    }
}
