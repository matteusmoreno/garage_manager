package br.com.matteusmoreno.garage_manager.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "viacep-api")
public interface ViaCepClient {

    @GET
    @Path("/{cep}/json")
    AddressApiResponse findAddressByCep(String cep);
}
