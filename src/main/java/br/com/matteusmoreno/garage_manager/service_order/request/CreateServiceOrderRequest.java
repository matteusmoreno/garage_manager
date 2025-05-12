package br.com.matteusmoreno.garage_manager.service_order.request;

import br.com.matteusmoreno.garage_manager.service_order_product.request.CreateServiceOrderProductRequest;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateServiceOrderRequest(
        @NotNull(message = "Motorcycle ID is required")
        @Schema(defaultValue = "motorcycle UUID")
        UUID motorcycleId,
        @Schema(defaultValue = "seller UUID")
        UUID sellerId,
        @Schema(defaultValue = "mechanic UUID")
        UUID mechanicId,
        List<CreateServiceOrderProductRequest> products,
        @Schema(defaultValue = "Service Order description")
        String description,
        @Schema(defaultValue = "100.00")
        BigDecimal laborPrice) {
}
