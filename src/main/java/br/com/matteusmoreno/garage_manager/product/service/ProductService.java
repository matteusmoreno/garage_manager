package br.com.matteusmoreno.garage_manager.product.service;

import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyDisabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductNotFoundException;
import br.com.matteusmoreno.garage_manager.product.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.product.repository.ProductRepository;
import br.com.matteusmoreno.garage_manager.product.entity.Product;
import br.com.matteusmoreno.garage_manager.product.request.CreateProductRequest;
import br.com.matteusmoreno.garage_manager.product.request.UpdateProductRequest;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    public ProductService(ProductRepository productRepository, MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .brand(request.brand().toUpperCase())
                .purchasePrice(request.purchasePrice())
                .salePrice(request.salePrice())
                .stockQuantity(request.stockQuantity())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)
                .build();

        meterRegistry.counter("product_created").increment();

        productRepository.persist(product);
        return product;
    }

    public Product findProductById(Long id) {
        if (productRepository.findById(id) == null) {
            meterRegistry.counter("product_not_found").increment();
            throw new ProductNotFoundException("Product not found");
        }

        Product product = productRepository.findById(id);
        meterRegistry.counter("product_found_by_id").increment();

        return product;
    }


    public List<ProductDetailsResponse> findProductsByNameContaining(String name) {
        int page = 0;
        int pageSize = 10;

        String[] keywords = name.split("\\s+");

        List<Product> products = productRepository.findByNameContainingIgnoreCasePaginated(keywords, page, pageSize);

        if (products.isEmpty()) {
            meterRegistry.counter("product_not_found").increment();
            throw new ProductNotFoundException("Product not found");
        }

        meterRegistry.counter("product_found_by_name").increment();

        return products.stream()
                .map(ProductDetailsResponse::new)
                .toList();
    }

    @Transactional
    public Product updateProduct(UpdateProductRequest request) {
        Product product = findProductById(request.id());

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.brand() != null) product.setBrand(request.brand().toUpperCase());
        if (request.purchasePrice() != null) product.setPurchasePrice(request.purchasePrice());
        if (request.salePrice() != null) product.setSalePrice(request.salePrice());
        if (request.stockQuantity() != null) product.setStockQuantity(request.stockQuantity());

        meterRegistry.counter("product_updated").increment();

        product.setUpdatedAt(LocalDateTime.now());
        productRepository.persist(product);

        return product;
    }

    @Transactional
    public void disableProductById(Long id) {
        Product product = findProductById(id);

        if (!product.getIsActive()) {
            meterRegistry.counter("product_already_disabled").increment();
            throw new ProductIsAlreadyDisabledException("Product is already disabled");
        }

        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(false);

        meterRegistry.counter("product_disabled").increment();

        productRepository.persist(product);
    }
    
    @Transactional
    public Product enableProductById(Long id) {
        Product product = findProductById(id);

        if (product.getIsActive()) {
            meterRegistry.counter("product_already_enabled").increment();
            throw new ProductIsAlreadyEnabledException("Product is already enabled");
        }

        product.setUpdatedAt(LocalDateTime.now());
        product.setDeletedAt(null);
        product.setIsActive(true);
        productRepository.persist(product);

        meterRegistry.counter("product_enabled").increment();

        return product;
    }
}
