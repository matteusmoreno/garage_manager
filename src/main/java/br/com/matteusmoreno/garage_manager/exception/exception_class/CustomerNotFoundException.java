package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
