package br.com.matteusmoreno.garage_manager.motorcycle.response;

import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;

import java.time.LocalDateTime;
import java.util.UUID;

public record MotorcycleDetailsResponse(
        UUID id,
        String brand,
        String model,
        String year,
        String color,
        String licensePlate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean isActive) {

    public MotorcycleDetailsResponse (Motorcycle motorcycle) {
        this(
                motorcycle.getId(),
                motorcycle.getBrand().getDisplayName(),
                motorcycle.getModel(),
                motorcycle.getYear(),
                motorcycle.getColor(),
                motorcycle.getLicensePlate(),
                motorcycle.getCreatedAt(),
                motorcycle.getUpdatedAt(),
                motorcycle.getDeletedAt(),
                motorcycle.getIsActive()
        );
    }


}
