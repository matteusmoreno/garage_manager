package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyDisabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductNotFoundException;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.ropository.ProductRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @InjectMock
    private ProductRepository productRepository;

    @Inject
    MeterRegistry meterRegistry;

    @Inject
    ProductService productService;

    private CreateProductRequest createProductRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        createProductRequest = new CreateProductRequest("Product Test", "Description Test", "Brand Test", BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00));
        product = Product.builder()
                .id(1L)
                .name("Product Test")
                .description("Description Test")
                .brand("Brand Test")
                .purchasePrice(BigDecimal.valueOf(10.00))
                .salePrice(BigDecimal.valueOf(20.00))
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)
                .build();

        // Criar um mock manualmente
        meterRegistry = mock(MeterRegistry.class);
        when(meterRegistry.counter(anyString())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
    }

    @Test
    @DisplayName("Should create product correctly")
    void shouldCreateProductCorrectly() {
        Product response = productService.createProduct(createProductRequest);

        verify(productRepository, times(1)).persist(response);
    }

    @Test
    @DisplayName("Should find product by id correctly")
    void shouldFindProductByIdCorrectly() {
        when(productRepository.findById(product.getId())).thenReturn(product);

        Product result = productService.findProductById(product.getId());

        verify(productRepository, times(2)).findById(product.getId());
        assertAll(
                () -> assertEquals(product.getId(), result.getId()),
                () -> assertEquals(product.getName(), result.getName()),
                () -> assertEquals(product.getDescription(), result.getDescription()),
                () -> assertEquals(product.getBrand(), result.getBrand()),
                () -> assertEquals(product.getPurchasePrice(), result.getPurchasePrice()),
                () -> assertEquals(product.getSalePrice(), result.getSalePrice()),
                () -> assertEquals(product.getCreatedAt(), result.getCreatedAt()),
                () -> assertNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found by id")
    void shouldThrowProductNotFoundExceptionWhenProductNotFound() {
        when(productRepository.findById(product.getId())).thenReturn(null);

        assertThrows(ProductNotFoundException.class,
                () -> productService.findProductById(product.getId()));

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("Should find products by name containing correctly")
    void shouldFindProductsByNameContainingCorrectly() {
        String searchName = "pro";
        String[] keywords = searchName.split("\\s+");
        int page = 0;
        int pageSize = 10;

        List<Product> products = List.of(product);

        when(productRepository.findByNameContainingIgnoreCasePaginated(keywords, page, pageSize))
                .thenReturn(products);

        List<ProductDetailsResponse> result = productService.findProductsByNameContaining(searchName);

        verify(productRepository, times(1)).findByNameContainingIgnoreCasePaginated(keywords, page, pageSize);

        assertAll(
                () -> assertEquals(products.size(), result.size()),
                () -> assertEquals(products.getFirst().getId(), result.getFirst().id()),
                () -> assertEquals(products.getFirst().getName(), result.getFirst().name()),
                () -> assertEquals(products.getFirst().getDescription(), result.getFirst().description()),
                () -> assertEquals(products.getFirst().getBrand(), result.getFirst().brand()),
                () -> assertEquals(products.getFirst().getPurchasePrice(), result.getFirst().purchasePrice()),
                () -> assertEquals(products.getFirst().getSalePrice(), result.getFirst().salePrice()),
                () -> assertEquals(products.getFirst().getCreatedAt(), result.getFirst().createdAt()),
                () -> assertNull(result.getFirst().updatedAt()),
                () -> assertNull(result.getFirst().deletedAt()),
                () -> assertTrue(result.getFirst().isActive())
        );
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found by name containing")
    void shouldThrowProductNotFoundExceptionWhenProductNotFoundByNameContaining() {
        String searchName = "p";
        String[] keywords = searchName.split("\\s+");
        int page = 0;
        int pageSize = 10;

        when(productRepository.findByNameContainingIgnoreCasePaginated(keywords, page, pageSize))
                .thenReturn(List.of());

        assertThrows(ProductNotFoundException.class,
                () -> productService.findProductsByNameContaining(searchName));

        verify(productRepository, times(1)).findByNameContainingIgnoreCasePaginated(keywords, page, pageSize);
    }

    @Test
    @DisplayName("Should update product correctly")
    void shouldUpdateProductCorrectly() {
        when(productRepository.findById(product.getId())).thenReturn(product);

        UpdateProductRequest updateProductRequest = new UpdateProductRequest(product.getId(), "New Product name", "New Description", "New Brand", BigDecimal.valueOf(30.00), BigDecimal.valueOf(40.00));

        ProductDetailsResponse result = productService.updateProduct(updateProductRequest);

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(1)).persist(product);

        assertAll(
                () -> assertEquals(updateProductRequest.id(), result.id()),
                () -> assertEquals(updateProductRequest.name(), result.name()),
                () -> assertEquals(updateProductRequest.description(), result.description()),
                () -> assertEquals(updateProductRequest.brand().toUpperCase(), result.brand()),
                () -> assertEquals(updateProductRequest.purchasePrice(), result.purchasePrice()),
                () -> assertEquals(updateProductRequest.salePrice(), result.salePrice()),
                () -> assertEquals(product.getCreatedAt(), result.createdAt()),
                () -> assertNotNull(result.updatedAt()),
                () -> assertNull(result.deletedAt()),
                () -> assertTrue(result.isActive())
        );
    }

    @Test
    @DisplayName("Should disable product correctly")
    void shouldDisableProductCorrectly() {
        when(productRepository.findById(product.getId())).thenReturn(product);

        productService.disableProductById(product.getId());

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(1)).persist(product);

        assertAll(
                () -> assertNotNull(product.getDeletedAt()),
                () -> assertFalse(product.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw ProductIsAlreadyDisabledException when product is already disabled")
    void shouldThrowProductIsAlreadyDisabledExceptionWhenProductIsAlreadyDisabled() {
        when(productRepository.findById(product.getId())).thenReturn(product);
        product.setIsActive(false);

        assertThrows(ProductIsAlreadyDisabledException.class,
                () -> productService.disableProductById(product.getId()));

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(0)).persist(product);
    }

    @Test
    @DisplayName("Should enable product correctly")
    void shouldEnableProductCorrectly() {
        when(productRepository.findById(product.getId())).thenReturn(product);
        product.setIsActive(false);

        ProductDetailsResponse result = productService.enableProductById(product.getId());

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(1)).persist(product);

        assertAll(
                () -> assertEquals(product.getId(), result.id()),
                () -> assertEquals(product.getName(), result.name()),
                () -> assertEquals(product.getDescription(), result.description()),
                () -> assertEquals(product.getBrand(), result.brand()),
                () -> assertEquals(product.getPurchasePrice(), result.purchasePrice()),
                () -> assertEquals(product.getSalePrice(), result.salePrice()),
                () -> assertEquals(product.getCreatedAt(), result.createdAt()),
                () -> assertNotNull(result.updatedAt()),
                () -> assertNull(result.deletedAt()),
                () -> assertTrue(result.isActive())
        );
    }

    @Test
    @DisplayName("Should throw ProductIsAlreadyEnabledException when product is already enabled")
    void shouldThrowProductIsAlreadyEnabledExceptionWhenProductIsAlreadyEnabled() {
        when(productRepository.findById(product.getId())).thenReturn(product);
        product.setIsActive(true);

        assertThrows(ProductIsAlreadyEnabledException.class,
                () -> productService.enableProductById(product.getId()));

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(0)).persist(product);
    }


}