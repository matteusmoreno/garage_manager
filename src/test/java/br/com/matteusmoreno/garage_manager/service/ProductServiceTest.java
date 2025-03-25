package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyDisabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductNotFoundException;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.repository.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product Service Tests")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    MeterRegistry meterRegistry;

    @InjectMocks
    ProductService productService;

    private CreateProductRequest createProductRequest;
    private Product product;
    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    void setUp() {
        createProductRequest = new CreateProductRequest("Product Test", "Description Test", "Brand Test", BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00));
        product = new Product(1L, "Product Test", "Description Test", "Brand Test", BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00), LocalDateTime.of(2020, 1, 1, 0, 0), null, null, true);
        updateProductRequest = new UpdateProductRequest(product.getId(), "New Product name", "New Description", "New Brand", BigDecimal.valueOf(30.00), BigDecimal.valueOf(40.00));


        setupMeterRegistry();
    }

    @Test
    @DisplayName("Should create product correctly")
    void shouldCreateProductCorrectly() {
        Product response = productService.createProduct(createProductRequest);

        verify(productRepository, times(1)).persist(response);
        assertAll(
                () -> assertEquals(createProductRequest.name(), response.getName()),
                () -> assertEquals(createProductRequest.description(), response.getDescription()),
                () -> assertEquals(createProductRequest.brand().toUpperCase(), response.getBrand()),
                () -> assertEquals(createProductRequest.purchasePrice(), response.getPurchasePrice()),
                () -> assertEquals(createProductRequest.salePrice(), response.getSalePrice()),
                () -> assertNotNull(response.getCreatedAt()),
                () -> assertNull(response.getUpdatedAt()),
                () -> assertNull(response.getDeletedAt()),
                () -> assertTrue(response.getIsActive())
        );
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

        Product result = productService.updateProduct(updateProductRequest);

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(1)).persist(product);

        assertAll(
                () -> assertEquals(updateProductRequest.id(), result.getId()),
                () -> assertEquals(updateProductRequest.name(), result.getName()),
                () -> assertEquals(updateProductRequest.description(), result.getDescription()),
                () -> assertEquals(updateProductRequest.brand().toUpperCase(), result.getBrand()),
                () -> assertEquals(updateProductRequest.purchasePrice(), result.getPurchasePrice()),
                () -> assertEquals(updateProductRequest.salePrice(), result.getSalePrice()),
                () -> assertEquals(product.getCreatedAt(), result.getCreatedAt()),
                () -> assertNotNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())
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

        Product result = productService.enableProductById(product.getId());

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(1)).persist(product);

        assertAll(
                () -> assertEquals(product.getId(), result.getId()),
                () -> assertEquals(product.getName(), result.getName()),
                () -> assertEquals(product.getDescription(), result.getDescription()),
                () -> assertEquals(product.getBrand(), result.getBrand()),
                () -> assertEquals(product.getPurchasePrice(), result.getPurchasePrice()),
                () -> assertEquals(product.getSalePrice(), result.getSalePrice()),
                () -> assertEquals(product.getCreatedAt(), result.getCreatedAt()),
                () -> assertNotNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getIsActive())
        );
    }

    @Test
    @DisplayName("Should throw ProductIsAlreadyEnabledException when product is already enabled")
    void shouldThrowProductIsAlreadyEnabledExceptionWhenProductIsAlreadyEnabled() {
        when(productRepository.findById(product.getId())).thenReturn(product);
        product.setIsActive(true);

        assertThrows(ProductIsAlreadyEnabledException.class, () -> productService.enableProductById(product.getId()));

        verify(productRepository, times(2)).findById(product.getId());
        verify(productRepository, times(0)).persist(product);
    }

    private void setupMeterRegistry() {
        Counter counter = mock(Counter.class);
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        doNothing().when(counter).increment();
    }
}