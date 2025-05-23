package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class InvalidServiceOrderStatusException extends RuntimeException {
    public InvalidServiceOrderStatusException(String message) {
        super(message);
    }
}
