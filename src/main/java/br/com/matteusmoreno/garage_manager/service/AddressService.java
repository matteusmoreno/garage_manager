package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.client.AddressApiResponse;
import br.com.matteusmoreno.garage_manager.client.ViaCepClient;
import br.com.matteusmoreno.garage_manager.domain.Address;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ZipCodeNotFoundException;
import br.com.matteusmoreno.garage_manager.ropository.AddressRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AddressService {

    @RestClient
    ViaCepClient viaCepClient;

    private final AddressRepository addressRepository;
    private final MeterRegistry meterRegistry;

    public AddressService(AddressRepository addressRepository, MeterRegistry meterRegistry) {
        this.addressRepository = addressRepository;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public Address createAddress(String zipCode, String number, String complement) {
        if (viaCepClient.findAddressByCep(zipCode) == null) {
            throw new ZipCodeNotFoundException("Zip code not found");
        }

        Address existingAddress = addressRepository.findByZipCodeAndNumber(zipCode, number);
        if (existingAddress != null) {
            return existingAddress;
        }

        AddressApiResponse apiResponse = viaCepClient.findAddressByCep(zipCode);

        Address address = Address.builder()
            .zipCode(apiResponse.cep())
            .street(apiResponse.logradouro())
            .neighborhood(apiResponse.bairro())
            .number(number)
            .city(apiResponse.localidade())
            .state(apiResponse.uf())
            .complement(complement)
            .build();

        meterRegistry.counter("address_created").increment();

        addressRepository.persist(address);

        return address;
    }
}
