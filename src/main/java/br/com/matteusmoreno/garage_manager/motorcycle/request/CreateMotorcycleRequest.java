package br.com.matteusmoreno.garage_manager.motorcycle.request;

import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMotorcycleRequest(
        @NotNull(message = "Motorcycle brand is required")
        MotorcycleBrand brand,
        @NotBlank(message = "Motorcycle model is required")
        String model,
        @NotBlank(message = "Motorcycle year is required")
        String year,
        @NotBlank(message = "Motorcycle color is required")
        String color,
        @NotBlank(message = "Motorcycle license plate is required")
        String licensePlate) {
}
