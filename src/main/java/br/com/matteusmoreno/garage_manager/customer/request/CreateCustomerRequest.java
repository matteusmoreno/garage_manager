package br.com.matteusmoreno.garage_manager.customer.request;

import br.com.matteusmoreno.garage_manager.motorcycle.request.CreateMotorcycleRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public record CreateCustomerRequest(
        @NotBlank(message = "Name is required")
        @Schema(defaultValue = "João da Silva")
        String name,
        @NotNull(message = "Birth date is required")
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in the format dd/MM/yyyy")
        @Schema(defaultValue = "01/01/1990")
        String birthDate,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "Phone must be in the format (99)99999-9999")
        @Schema(defaultValue = "(00)00000-0000")
        String phone,
        @Email(message = "Email must be a valid email")
        @Schema(defaultValue = "joao.silva@email.com")
        String email,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format 123.456.789-00")
        @Schema(defaultValue = "176.372.110-88")
        String cpf,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must be in the format 12345-678")
        @Schema(defaultValue = "01001-000")
        String zipCode,
        @Schema(defaultValue = "00")
        String addressNumber,
        @Schema(defaultValue = "Casa")
        String addressComplement,
        List<CreateMotorcycleRequest> motorcycles
        ) {
}
