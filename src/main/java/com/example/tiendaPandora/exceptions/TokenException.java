package com.example.tiendaPandora.exceptions;

public class TokenException extends RuntimeException{

    public TokenException(String message) {
        super(message);
    }

    public TokenException() {
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
