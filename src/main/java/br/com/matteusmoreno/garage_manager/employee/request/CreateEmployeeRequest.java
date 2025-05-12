package br.com.matteusmoreno.garage_manager.employee.request;

import br.com.matteusmoreno.garage_manager.employee.constant.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record CreateEmployeeRequest(
        @NotBlank(message = "Username is required")
        @Schema(defaultValue = "maria")
        String username,
        @NotBlank(message = "Password is required")
        @Schema(defaultValue = "maria123")
        String password,
        @NotBlank(message = "Name is required")
        @Schema(defaultValue = "Maria do Carmo")
        String name,
        @Email(message = "Email must be a valid email")
        @Schema(defaultValue = "mariadocarmo@email.com")
        String email,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "Phone must be in the format (99)99999-9999")
        @Schema(defaultValue = "(99)99999-9999")
        String phone,
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in the format dd/MM/yyyy")
        @Schema(defaultValue = "01/01/1987")
        String birthDate,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format 123.456.789-00")
        @Schema(defaultValue = "885.671.660-71")
        String cpf,
        @NotNull(message = "Role is required")
        @Schema(defaultValue = "SELLER")
        EmployeeRole role,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must be in the format 12345-678")
        @Schema(defaultValue = "01001-000")
        String zipCode,
        @Schema(defaultValue = "569")
        String addressNumber,
        @Schema(defaultValue = "Apto 101")
        String addressComplement) {
}
