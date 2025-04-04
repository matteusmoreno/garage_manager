package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class InsufficientProductQuantityException extends RuntimeException {
    public InsufficientProductQuantityException(String message) {
        super(message);
    }
}
