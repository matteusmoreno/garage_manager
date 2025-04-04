package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.ServiceOrderNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleServiceOrderNotFoundException implements ExceptionMapper<ServiceOrderNotFoundException> {

    @Override
    public Response toResponse(ServiceOrderNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}
