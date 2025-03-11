package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class ProductIsAlreadyEnabledException extends RuntimeException {
    public ProductIsAlreadyEnabledException(String message) {
        super(message);
    }
}
