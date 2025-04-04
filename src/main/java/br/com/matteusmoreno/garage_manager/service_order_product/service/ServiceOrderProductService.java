package br.com.matteusmoreno.garage_manager.service_order_product.service;

import br.com.matteusmoreno.garage_manager.exception.exception_class.InsufficientProductQuantityException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductNotFoundException;
import br.com.matteusmoreno.garage_manager.product.entity.Product;
import br.com.matteusmoreno.garage_manager.product.repository.ProductRepository;
import br.com.matteusmoreno.garage_manager.service_order_product.entity.ServiceOrderProduct;
import br.com.matteusmoreno.garage_manager.service_order_product.repository.ServiceOrderProductRepository;
import br.com.matteusmoreno.garage_manager.service_order_product.request.CreateServiceOrderProductRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class ServiceOrderProductService {

    private final ProductRepository productRepository;

    public ServiceOrderProductService(ServiceOrderProductRepository serviceOrderProductRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ServiceOrderProduct createServiceOrderProduct(CreateServiceOrderProductRequest request) {
        if (productRepository.findById(request.productId()) == null) throw new ProductNotFoundException("Product not found: " + request.productId());

        Product product = productRepository.findById(request.productId());

        if (product.getStockQuantity() < request.quantity()) throw new InsufficientProductQuantityException("Insufficient product quantity: " + product.getName());

        return ServiceOrderProduct.builder()
                .product(product)
                .quantity(request.quantity())
                .unitaryPrice(product.getSalePrice())
                .finalPrice(product.getSalePrice().multiply(BigDecimal.valueOf(request.quantity())))
                .build();
    }
}
