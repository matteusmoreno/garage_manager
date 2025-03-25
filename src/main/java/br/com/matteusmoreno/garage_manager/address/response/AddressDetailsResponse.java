package br.com.matteusmoreno.garage_manager.address.response;

import br.com.matteusmoreno.garage_manager.address.entity.Address;

public record AddressDetailsResponse(
        String zipCode,
        String street,
        String neighborhood,
        String number,
        String city,
        String state,
        String complement) {

    public AddressDetailsResponse(Address address){
        this(
                address.getZipCode(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getNumber(),
                address.getCity(),
                address.getState(),
                address.getComplement()
        );
    }

}
