package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.exception.exception_handler.ProductNotFoundException;
import br.com.matteusmoreno.garage_manager.request.UpdateProductRequest;
import br.com.matteusmoreno.garage_manager.response.ProductDetailsResponse;
import br.com.matteusmoreno.garage_manager.ropository.ProductRepository;
import br.com.matteusmoreno.garage_manager.domain.Product;
import br.com.matteusmoreno.garage_manager.request.CreateProductRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public Product findProductById(Long id) {
        if (productRepository.findById(id) == null) {
            throw new ProductNotFoundException("Product not found");
        }
        return productRepository.findById(id);
    }


    public List<ProductDetailsResponse> findProductsByNameContaining(String name) {
        int page = 0;
        int pageSize = 10;

        String[] keywords = name.split("\\s+");

        List<Product> products = productRepository.findByNameContainingIgnoreCasePaginated(keywords, page, pageSize);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        return products.stream()
                .map(ProductDetailsResponse::new)
                .toList();
    }

    @Transactional
    public ProductDetailsResponse updateProduct(UpdateProductRequest request) {
        if (productRepository.findById(request.id()) == null) {
            throw new ProductNotFoundException("Product not found");
        }

        Product product = findProductById(request.id());

        if (request.name() != null) {
            product.setName(request.name());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.brand() != null) {
            product.setBrand(request.brand().toUpperCase());
        }
        if (request.purchasePrice() != null) {
            product.setPurchasePrice(request.purchasePrice());
        }
        if (request.salePrice() != null) {
            product.setSalePrice(request.salePrice());
        }

        product.setUpdatedAt(LocalDateTime.now());
        productRepository.persist(product);

        return new ProductDetailsResponse(product);
    }
}
