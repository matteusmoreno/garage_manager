package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class MotorcycleIsAlreadyDisabledException extends RuntimeException {
    public MotorcycleIsAlreadyDisabledException(String message) {
        super(message);
    }
}
