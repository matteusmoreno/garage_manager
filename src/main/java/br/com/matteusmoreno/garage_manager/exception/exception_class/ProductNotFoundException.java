package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
