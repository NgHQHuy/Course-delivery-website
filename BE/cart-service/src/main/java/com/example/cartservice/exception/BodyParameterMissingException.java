package com.example.cartservice.exception;

public class BodyParameterMissingException extends RuntimeException{
    public BodyParameterMissingException(String message) {
        super(message);
    }
}
