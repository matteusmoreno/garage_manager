package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class EmployeeIsAlreadyDisabledException extends RuntimeException {
    public EmployeeIsAlreadyDisabledException(String message) {
        super(message);
    }
}
