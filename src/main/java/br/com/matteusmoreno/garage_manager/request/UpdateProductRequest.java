package br.com.matteusmoreno.garage_manager.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull(message = "Id is required")
        Long id,
        String name,
        String description,
        String brand,
        BigDecimal purchasePrice,
        BigDecimal salePrice) {
}
