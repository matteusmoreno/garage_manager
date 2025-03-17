package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class CustomerIsAlreadyDisabledException extends RuntimeException {
    public CustomerIsAlreadyDisabledException(String message) {
        super(message);
    }
}
