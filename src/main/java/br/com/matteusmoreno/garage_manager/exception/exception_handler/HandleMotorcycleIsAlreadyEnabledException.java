package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleIsAlreadyEnabledException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleMotorcycleIsAlreadyEnabledException implements ExceptionMapper<MotorcycleIsAlreadyEnabledException> {

    @Override
    public Response toResponse(MotorcycleIsAlreadyEnabledException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }
}
