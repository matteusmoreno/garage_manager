package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.CustomerIsAlreadyDisabledException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleCustomerIsAlreadyDisabledException implements ExceptionMapper<CustomerIsAlreadyDisabledException> {

    @Override
    public Response toResponse(CustomerIsAlreadyDisabledException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }
}
