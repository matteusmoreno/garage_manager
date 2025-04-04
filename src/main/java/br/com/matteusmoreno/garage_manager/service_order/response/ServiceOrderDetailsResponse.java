package br.com.matteusmoreno.garage_manager.service_order.response;

import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import br.com.matteusmoreno.garage_manager.service_order_product.response.ServiceOrderProductResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderDetailsResponse(
        Long id,
        String customerName,
        MotorcycleBrand motorcycleBrand,
        String motorcycleModel,
        String motorcycleYear,
        String motorcycleColor,
        String sellerName,
        String mechanicName,
        List<ServiceOrderProductResponse> products,
        BigDecimal laborPrice,
        BigDecimal totalCost,
        String description,
        String serviceOrderStatus,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime updatedAt,
        LocalDateTime finishedAt,
        LocalDateTime canceledAt
        ) {

    public ServiceOrderDetailsResponse(ServiceOrder serviceOrder) {
        this(
                serviceOrder.getId(),
                serviceOrder.getMotorcycle().getCustomer().getName(),
                serviceOrder.getMotorcycle().getBrand(),
                serviceOrder.getMotorcycle().getModel(),
                serviceOrder.getMotorcycle().getYear(),
                serviceOrder.getMotorcycle().getColor(),
                serviceOrder.getSeller().getName(),
                serviceOrder.getMechanic().getName(),
                serviceOrder.getProducts().stream()
                        .map(ServiceOrderProductResponse::new)
                        .toList(),
                serviceOrder.getLaborPrice(),
                serviceOrder.getTotalCost(),
                serviceOrder.getDescription(),
                serviceOrder.getServiceOrderStatus().getDisplayName(),
                serviceOrder.getCreatedAt(),
                serviceOrder.getStartedAt(),
                serviceOrder.getUpdatedAt(),
                serviceOrder.getFinishedAt(),
                serviceOrder.getCanceledAt()
        );
    }
}
