package br.com.matteusmoreno.garage_manager.product.response;

import br.com.matteusmoreno.garage_manager.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String brand,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean isActive) {
    public ProductDetailsResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getPurchasePrice(),
                product.getSalePrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getDeletedAt(),
                product.getIsActive()
        );
    }
}
