package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class EmployeeIsAlreadyEnabledException extends RuntimeException {
    public EmployeeIsAlreadyEnabledException(String message) {
        super(message);
    }
}
