package br.com.matteusmoreno.garage_manager.motorcycle.request;

import br.com.matteusmoreno.garage_manager.motorcycle.constant.MotorcycleBrand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CreateMotorcycleRequest(
        @NotNull(message = "Motorcycle brand is required")
        @Schema(defaultValue = "HONDA")
        MotorcycleBrand brand,

        @NotBlank(message = "Motorcycle model is required")
        @Schema(defaultValue = "CG 160 Fan")
        String model,

        @NotBlank(message = "Motorcycle year is required")
        @Schema(defaultValue = "2020")
        String year,

        @NotBlank(message = "Motorcycle color is required")
        @Schema(defaultValue = "Preta")
        String color,

        @NotBlank(message = "Motorcycle license plate is required")
        @Schema(defaultValue = "ABC1D23")
        String licensePlate
) {
}
