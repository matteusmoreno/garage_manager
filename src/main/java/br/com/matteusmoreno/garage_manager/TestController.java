package br.com.matteusmoreno.garage_manager;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test-controller")
public class TestController {

    @GET
    public String test() {
        return "Test controller is working!";
    }
}
