package br.com.matteusmoreno.garage_manager.service_order.request;

import br.com.matteusmoreno.garage_manager.service_order_product.request.CreateServiceOrderProductRequest;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateServiceOrderRequest(
        @NotNull(message = "Motorcycle ID is required")
        UUID motorcycleId,
        UUID sellerId,
        UUID mechanicId,
        List<CreateServiceOrderProductRequest> products,
        String description,
        BigDecimal laborPrice) {
}
