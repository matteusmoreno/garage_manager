package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
