package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class ProductIsAlreadyDisabledException extends RuntimeException {
    public ProductIsAlreadyDisabledException(String message) {
        super(message);
    }
}
