package br.com.matteusmoreno.garage_manager.controller;

import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.service.ProductService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Path("/create")
    public Response create(CreateProductRequest request, @Context UriInfo uriInfo) {
        Product product = productService.createProduct(request);
        URI uri = uriInfo.getAbsolutePathBuilder().build();

        return Response.created(uri).entity(new ProductDetailsResponse(product)).build();
    }
}
