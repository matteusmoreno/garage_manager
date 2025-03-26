package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class MotorcycleIsAlreadyEnabledException extends RuntimeException {
    public MotorcycleIsAlreadyEnabledException(String message) {
        super(message);
    }
}
