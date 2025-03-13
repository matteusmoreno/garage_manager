package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class ZipCodeNotFoundException extends RuntimeException {
    public ZipCodeNotFoundException(String message) {
        super(message);
    }
}
