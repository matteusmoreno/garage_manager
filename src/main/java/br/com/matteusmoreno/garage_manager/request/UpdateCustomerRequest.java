package br.com.matteusmoreno.garage_manager.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UpdateCustomerRequest(
        @NotNull(message = "Id is required")
        UUID id,
        String name,
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in the format dd/MM/yyyy")
        String birthDate,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "Phone must be in the format (99)99999-9999")
        String phone,
        @Email(message = "Email must be a valid email")
        String email,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format 123.456.789-00")
        String cpf,
        String zipCode,
        String addressNumber,
        String addressComplement) {

        @AssertTrue(message = "If zip code is provided, address number must also be provided")
        public boolean isAddressValid() {
                return zipCode == null || addressNumber != null && !addressNumber.isBlank();
        }

}
