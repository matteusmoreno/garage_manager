package br.com.matteusmoreno.garage_manager.response;

import br.com.matteusmoreno.garage_manager.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String brand,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
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
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getDeletedAt(),
                product.getIsActive()
        );
    }
}
