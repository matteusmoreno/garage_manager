package br.com.matteusmoreno.garage_manager.exception.exception_handler;

import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.ProductNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleProductIsAlreadyEnabledException implements ExceptionMapper<ProductIsAlreadyEnabledException> {

    @Override
    public Response toResponse(ProductIsAlreadyEnabledException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }

}
