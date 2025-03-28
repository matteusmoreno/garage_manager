package br.com.matteusmoreno.garage_manager.employee.request;

import br.com.matteusmoreno.garage_manager.employee.constant.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateEmployeeRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Email must be a valid email")
        String email,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "Phone must be in the format (99)99999-9999")
        String phone,
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in the format dd/MM/yyyy")
        String birthDate,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format 123.456.789-00")
        String cpf,
        @NotNull(message = "Role is required")
        EmployeeRole role,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must be in the format 12345-678")
        String zipCode,
        String addressNumber,
        String addressComplement) {
}
