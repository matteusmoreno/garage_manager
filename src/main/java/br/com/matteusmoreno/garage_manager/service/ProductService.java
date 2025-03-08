package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.ropository.ProductRepository;
import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .brand(request.brand().toUpperCase())
                .purchasePrice(request.purchasePrice())
                .salePrice(request.salePrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)
                .build();

        productRepository.persist(product);

        return product;
    }
}
