package br.com.matteusmoreno.garage_manager.response;

import br.com.matteusmoreno.garage_manager.domain.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDetailsResponse(
        UUID id,
        String name,
        String birthDate,
        Integer age,
        String phone,
        String email,
        String cpf,
        AddressDetailsResponse address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean isActive
        ) {

    public CustomerDetailsResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getName(),
                customer.getBirthDate(),
                customer.getAge(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getCpf(),
                new AddressDetailsResponse(customer.getAddress()),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getIsActive()
        );
    }
}
