package br.com.matteusmoreno.garage_manager.login;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.employee.repository.EmployeeRepository;
import br.com.matteusmoreno.garage_manager.exception.exception_class.InvalidCredentialsException;
import br.com.matteusmoreno.garage_manager.utils.UtilsService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class LoginService {

    private final EmployeeRepository employeeRepository;
    private final UtilsService utilsService;

    public LoginService(EmployeeRepository employeeRepository, UtilsService utilsService) {
        this.employeeRepository = employeeRepository;
        this.utilsService = utilsService;
    }

    public String login(LoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.username());

        if (!utilsService.verifyPassword(request.password(), employee.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return Jwt
                .issuer("https://garage-manager.io")
                .upn(employee.getUsername())
                .subject(employee.getId().toString())
                .groups(Set.of(employee.getRole().name()))
                .expiresIn(Duration.ofHours(2))
                .sign();
    }
}
