package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
