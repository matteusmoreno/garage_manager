package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.InvalidServiceOrderStatusException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleInvalidServiceOrderStatusException implements ExceptionMapper<InvalidServiceOrderStatusException> {
    @Override
    public Response toResponse(InvalidServiceOrderStatusException e) {
        return Response
                .status(jakarta.ws.rs.core.Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }
}
