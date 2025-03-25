package br.com.matteusmoreno.garage_manager.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is mandatory")
        String name,
        String description,
        String brand,
        @NotNull(message = "Purchase price is mandatory")
        BigDecimal purchasePrice,
        @NotNull(message = "Sale price is mandatory")
        BigDecimal salePrice,
        @PositiveOrZero(message = "Stock quantity must be positive or zero")
        Integer stockQuantity
        ) {

}
