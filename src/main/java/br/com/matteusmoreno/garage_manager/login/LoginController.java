package br.com.matteusmoreno.garage_manager.login;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        String token = loginService.login(request);

        return Response.ok(token).build();
    }
}
