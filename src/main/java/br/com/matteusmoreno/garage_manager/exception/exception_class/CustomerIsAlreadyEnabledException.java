package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class CustomerIsAlreadyEnabledException extends RuntimeException {
    public CustomerIsAlreadyEnabledException(String message) {
        super(message);
    }
}
