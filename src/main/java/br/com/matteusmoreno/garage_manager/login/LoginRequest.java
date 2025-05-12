package br.com.matteusmoreno.garage_manager.login;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Schema(defaultValue = "admin")
        String username,
        @NotBlank(message = "Password is required")
        @Schema(defaultValue = "admin")
        String password) {
}
