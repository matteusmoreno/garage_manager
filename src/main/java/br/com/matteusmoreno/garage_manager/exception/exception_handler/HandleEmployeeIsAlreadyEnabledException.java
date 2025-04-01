package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.EmployeeIsAlreadyEnabledException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleEmployeeIsAlreadyEnabledException implements ExceptionMapper<EmployeeIsAlreadyEnabledException> {

    @Override
    public Response toResponse(EmployeeIsAlreadyEnabledException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
