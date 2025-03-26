package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class MotorcycleNotFoundException extends RuntimeException {
    public MotorcycleNotFoundException(String message) {
        super(message);
    }
}
