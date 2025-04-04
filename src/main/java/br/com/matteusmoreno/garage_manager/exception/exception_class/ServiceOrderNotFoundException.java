package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class ServiceOrderNotFoundException extends RuntimeException {
    public ServiceOrderNotFoundException(String message) {
        super(message);
    }
}
