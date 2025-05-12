package br.com.matteusmoreno.garage_manager.service_order_product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CreateServiceOrderProductRequest(
        @NotNull(message = "Product id is required")
        @Schema(defaultValue = "1")
        Long productId,
        @NotNull(message = "Quantity is required")
        @PositiveOrZero(message = "Quantity must be greater than or equal to zero")
        @Schema(defaultValue = "1")
        Integer quantity) {
}
