package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
