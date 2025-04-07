package br.com.matteusmoreno.garage_manager.product.controller;

import br.com.matteusmoreno.garage_manager.product.entity.Product;
import br.com.matteusmoreno.garage_manager.product.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.product.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.product.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.product.service.ProductService;
import jakarta.validation.Valid;
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
    public Response create(@Valid CreateProductRequest request, @Context UriInfo uriInfo) {
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
    public Response update(@Valid UpdateProductRequest request) {
        Product product = productService.updateProduct(request);

        return Response.ok(new ProductDetailsResponse(product)).build();
    }

    @DELETE
    @Path("/disable/{id}")
    public Response disableProduct(Long id) {
        productService.disableProductById(id);

        return Response.noContent().build();
    }

    @PATCH
    @Path("/enable/{id}")
    public Response enableProduct(Long id) {
        Product product = productService.enableProductById(id);

        return Response.ok(new ProductDetailsResponse(product)).build();
    }
}
