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
        if (isAdmin(request)) {
            return generateJwt(request.username(), "1", Set.of("ADMIN"));
        }

        Employee employee = employeeRepository.findByUsername(request.username());

        if (!utilsService.verifyPassword(request.password(), employee.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return generateJwt(employee.getUsername(), employee.getId().toString(), Set.of(employee.getRole().name()));
    }

    private boolean isAdmin(LoginRequest request) {
        return "admin".equals(request.username()) && "admin".equals(request.password());
    }

    private String generateJwt(String username, String subject, Set<String> roles) {
        return Jwt
                .issuer("https://garage-manager.io")
                .upn(username)
                .subject(subject)
                .groups(roles)
                .expiresIn(Duration.ofHours(2))
                .sign();
    }
}
