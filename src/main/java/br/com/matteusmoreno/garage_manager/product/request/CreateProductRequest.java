package br.com.matteusmoreno.garage_manager.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is mandatory")
        @Schema(defaultValue = "Óleo de motor")
        String name,
        @Schema(defaultValue = "Mobil Super™ Moto 4T 20W-50 é um óleo mineral de qualidade premium para motores de motocicletas de quatro tempos.")
        String description,
        @Schema(defaultValue = "Mobil")
        String brand,
        @NotNull(message = "Purchase price is mandatory")
        @Schema(defaultValue = "39.00")
        BigDecimal purchasePrice,
        @Schema(defaultValue = "39.00")
        @NotNull(message = "Sale price is mandatory")
        BigDecimal salePrice,
        @PositiveOrZero(message = "Stock quantity must be positive or zero")
        @Schema(defaultValue = "10")
        Integer stockQuantity
        ) {

}
