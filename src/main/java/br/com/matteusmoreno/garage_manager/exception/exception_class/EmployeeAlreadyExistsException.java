package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
