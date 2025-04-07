package br.com.matteusmoreno.garage_manager.login;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class LoginService {

    private final EmployeeRepository employeeRepository;

    public LoginService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String login(LoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.username());

        String jwt = Jwt.issuer("https://garage-manager.io")
                .upn(employee.getUsername())
                .subject(employee.getId().toString())
                .groups(Set.of(employee.getRole().name()))
                .expiresIn(Duration.ofHours(2))
                .sign();

        System.out.println("JWT: " + jwt);
        return jwt;
    }
}
