package br.com.matteusmoreno.garage_manager.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is mandatory")
        String name,
        String description,
        String brand,
        @NotNull(message = "Purchase price is mandatory")
        BigDecimal purchasePrice,
        @NotNull(message = "Sale price is mandatory")
        BigDecimal salePrice) {

}
