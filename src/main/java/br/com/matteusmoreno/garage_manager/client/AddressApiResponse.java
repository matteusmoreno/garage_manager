package br.com.matteusmoreno.garage_manager.client;

public record AddressApiResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf) {
}
