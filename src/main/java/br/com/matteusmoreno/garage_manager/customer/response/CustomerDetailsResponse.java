package br.com.matteusmoreno.garage_manager.customer.response;

import br.com.matteusmoreno.garage_manager.address.response.AddressDetailsResponse;
import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.motorcycle.response.MotorcycleDetailsResponse;

import java.time.LocalDateTime;
import java.util.List;
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
        List<MotorcycleDetailsResponse> motorcycles,
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
                customer.getMotorcycles().stream().map(MotorcycleDetailsResponse::new).toList(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getIsActive()
        );
    }
}
