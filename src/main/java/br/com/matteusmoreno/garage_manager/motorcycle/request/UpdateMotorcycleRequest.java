package br.com.matteusmoreno.garage_manager.motorcycle.request;

import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateMotorcycleRequest (
        @NotNull(message = "Motorcycle id is required")
        UUID id,
        MotorcycleBrand brand,
        String model,
        String year,
        String color,
        String licensePlate) {
}
