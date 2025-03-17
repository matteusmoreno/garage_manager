package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.CustomerIsAlreadyEnabledException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleCustomerIsAlreadyEnabledException implements ExceptionMapper<CustomerIsAlreadyEnabledException> {

    @Override
    public Response toResponse(CustomerIsAlreadyEnabledException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }
}
