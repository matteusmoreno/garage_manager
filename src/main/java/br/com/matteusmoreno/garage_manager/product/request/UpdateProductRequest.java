package br.com.matteusmoreno.garage_manager.product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull(message = "Id is required")
        Long id,
        String name,
        String description,
        String brand,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        @PositiveOrZero(message = "Stock quantity must be positive or zero")
        Integer stockQuantity) {
}
