package br.com.matteusmoreno.garage_manager.exception.exception_class;

public class CpfInvalidException extends RuntimeException {
    public CpfInvalidException(String message) {
        super(message);
    }
}
