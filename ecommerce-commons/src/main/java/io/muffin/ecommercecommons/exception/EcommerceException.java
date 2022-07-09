package io.muffin.ecommercecommons.exception;

public class EcommerceException extends RuntimeException{
    public EcommerceException() {
        super();
    }

    public EcommerceException(String message) {
        super(message);
    }

    public EcommerceException(String message, Throwable cause) {
        super(message, cause);
    }
}
