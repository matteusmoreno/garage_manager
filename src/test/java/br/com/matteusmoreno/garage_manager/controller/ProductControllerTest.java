package br.com.matteusmoreno.garage_manager.controller;

import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.service.ProductService;
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

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product Controller Tests")
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    UriInfo uriInfo;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    private CreateProductRequest createProductRequest;
    private Product product;
    private Product updatedProduct;
    private ProductDetailsResponse productDetailsResponse;
    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    void setUp() {
        createProductRequest = new CreateProductRequest("Product Test", "Description Test", "Brand Test", BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00), 0);
        product = new Product(1L, "Product Test", "Description Test", "BRAND TEST", BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00), 0, LocalDateTime.now(), null, null, true);
        productDetailsResponse = new ProductDetailsResponse(product);
        updateProductRequest = new UpdateProductRequest(1L, "Updated Product Test", "Updated Description Test", "Updated Brand Test", BigDecimal.valueOf(50.00), BigDecimal.valueOf(70.00), 10);
        updatedProduct = new Product(1L, "Updated Product Test", "Updated Description Test", "UPDATED BRAND TEST", BigDecimal.valueOf(50.00), BigDecimal.valueOf(70.00), 10, LocalDateTime.now(), LocalDateTime.now(), null, true);
    }

    @Test
    @DisplayName("Should create a product")
    void shouldCreateProduct() {
        URI uri = URI.create("http://localhost:8080/products/create/1");

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(UriBuilder.fromUri(uri));
        when(productService.createProduct(createProductRequest)).thenReturn(product);

        Response response = productController.create(createProductRequest, uriInfo);

        verify(productService).createProduct(createProductRequest);


        assertAll(
                () -> assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus()),
                () -> assertEquals(uri, response.getLocation()),
                () -> assertEquals(product.getId(), productDetailsResponse.id()),
                () -> assertEquals(createProductRequest.name(), productDetailsResponse.name()),
                () -> assertEquals(createProductRequest.description(), productDetailsResponse.description()),
                () -> assertEquals(createProductRequest.brand().toUpperCase(), productDetailsResponse.brand()),
                () -> assertEquals(BigDecimal.valueOf(10.00), productDetailsResponse.purchasePrice()),
                () -> assertEquals(BigDecimal.valueOf(20.00), productDetailsResponse.salePrice()),
                () -> assertEquals(product.getStockQuantity(), productDetailsResponse.stockQuantity()),
                () -> assertNotNull(productDetailsResponse.createdAt()),
                () -> assertNull(productDetailsResponse.updatedAt()),
                () -> assertNull(productDetailsResponse.deletedAt()),
                () -> assertTrue(productDetailsResponse.isActive())
        );

    }

    @Test
    @DisplayName("Should find a product by id")
    void shouldFindProductById() {
        when(productService.findProductById(1L)).thenReturn(product);

        Response response = productController.findById(1L);
        ProductDetailsResponse responseBody = new ProductDetailsResponse(product);

        verify(productService).findProductById(1L);

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(product.getId(), responseBody.id()),
                () -> assertEquals(product.getName(), responseBody.name()),
                () -> assertEquals(product.getDescription(), responseBody.description()),
                () -> assertEquals(product.getBrand(), responseBody.brand()),
                () -> assertEquals(product.getPurchasePrice(), responseBody.purchasePrice()),
                () -> assertEquals(product.getSalePrice(), responseBody.salePrice()),
                () -> assertEquals(product.getStockQuantity(), responseBody.stockQuantity()),
                () -> assertEquals(product.getCreatedAt(), responseBody.createdAt()),
                () -> assertEquals(product.getUpdatedAt(), responseBody.updatedAt()),
                () -> assertEquals(product.getDeletedAt(), responseBody.deletedAt()),
                () -> assertEquals(product.getIsActive(), responseBody.isActive())
        );
    }


    @Test
    @DisplayName("Should find a product by name containing")
    void shouldFindProductByNameContaining() {
        when(productService.findProductsByNameContaining("Product Test")).thenReturn(List.of(productDetailsResponse));

        Response response = productController.findByNameContaining("Product Test");

        verify(productService).findProductsByNameContaining("Product Test");

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(List.of(productDetailsResponse), response.getEntity())
        );
    }

    @Test
    @DisplayName("Should update a product")
    void shouldUpdateProduct() {
        when(productService.updateProduct(updateProductRequest)).thenReturn(updatedProduct);

        Response response = productController.update(updateProductRequest);
        ProductDetailsResponse responseBody = new ProductDetailsResponse(updatedProduct);

        verify(productService).updateProduct(updateProductRequest);

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(product.getId(), responseBody.id()),
                () -> assertEquals(updateProductRequest.name(), responseBody.name()),
                () -> assertEquals(updateProductRequest.description(), responseBody.description()),
                () -> assertEquals(updateProductRequest.brand().toUpperCase(), responseBody.brand()),
                () -> assertEquals(updateProductRequest.purchasePrice(), responseBody.purchasePrice()),
                () -> assertEquals(updateProductRequest.salePrice(), responseBody.salePrice()),
                () -> assertEquals(updateProductRequest.stockQuantity(), responseBody.stockQuantity()),
                () -> assertNotNull(responseBody.createdAt()),
                () -> assertNotNull(responseBody.updatedAt()),
                () -> assertNull(responseBody.deletedAt()),
                () -> assertTrue(responseBody.isActive())
        );
    }

    @Test
    @DisplayName("Should disable a product")
    void shouldDisableProduct() {
        doNothing().when(productService).disableProductById(1L);
        Response response = productController.disableProduct(1L);

        verify(productService).disableProductById(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Should enable a product")
    void shouldEnableProduct() {
        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(false);

        when(productService.enableProductById(1L)).thenReturn(product);

        Response response = productController.enableProduct(1L);

        verify(productService).enableProductById(1L);

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(product.getId(), productDetailsResponse.id()),
                () -> assertEquals(product.getName(), productDetailsResponse.name()),
                () -> assertEquals(product.getDescription(), productDetailsResponse.description()),
                () -> assertEquals(product.getBrand().toUpperCase(), productDetailsResponse.brand()),
                () -> assertEquals(product.getPurchasePrice(), productDetailsResponse.purchasePrice()),
                () -> assertEquals(product.getSalePrice(), productDetailsResponse.salePrice()),
                () -> assertEquals(product.getStockQuantity(), productDetailsResponse.stockQuantity()),
                () -> assertEquals(product.getCreatedAt(), productDetailsResponse.createdAt()),
                () -> assertEquals(product.getUpdatedAt(), productDetailsResponse.updatedAt()),
                () -> assertNull(productDetailsResponse.deletedAt()),
                () -> assertTrue(productDetailsResponse.isActive())
        );
    }
}