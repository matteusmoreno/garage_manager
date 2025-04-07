package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
