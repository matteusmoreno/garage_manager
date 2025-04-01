package br.com.matteusmoreno.garage_manager.employee.request;

import br.com.matteusmoreno.garage_manager.employee.constant.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UpdateEmployeeRequest(
        @NotNull(message = "ID is required")
        UUID id,
        String username,
        String password,
        String name,
        String email,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "Phone must be in the format (99)99999-9999")
        String phone,
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in the format dd/MM/yyyy")
        String birthDate,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format 123.456.789-00")
        String cpf,
        EmployeeRole role,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must be in the format 12345-678")
        String zipCode,
        String addressNumber,
        String addressComplement) {
}
