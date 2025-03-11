package br.com.matteusmoreno.garage_manager.controller;

import br.com.matteusmoreno.garage_manager.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

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

    @GET
    @Path("/find-by-id/{id}")
    public Response findById(Long id) {
        Product product = productService.findProductById(id);

        return Response.ok(new ProductDetailsResponse(product)).build();
    }

    @GET
    @Path("/find-by-name/{name}")
    public Response findByNameContaining(String name) {
        List<ProductDetailsResponse> products = productService.findProductsByNameContaining(name);

        return Response.ok(products).build();
    }

    @PUT
    @Path("/update")
    public Response update(UpdateProductRequest request) {
        ProductDetailsResponse response = productService.updateProduct(request);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/disable/{id}")
    public Response disableProduct(Long id) {
        productService.disableProductById(id);

        return Response.noContent().build();
    }
}
