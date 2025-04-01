package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
