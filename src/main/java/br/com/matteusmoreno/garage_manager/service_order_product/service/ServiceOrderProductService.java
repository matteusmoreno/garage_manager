package br.com.matteusmoreno.garage_manager.service_order_product.service;

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
        Product product = productRepository.findById(request.productId());

        //CREATE A VALIDATION CLASS
        if (product.getStockQuantity() < request.quantity()) throw new IllegalArgumentException("Insufficient stock");

        return ServiceOrderProduct.builder()
                .product(product)
                .quantity(request.quantity())
                .unitaryPrice(product.getSalePrice())
                .finalPrice(product.getSalePrice().multiply(BigDecimal.valueOf(request.quantity())))
                .build();
    }
}
