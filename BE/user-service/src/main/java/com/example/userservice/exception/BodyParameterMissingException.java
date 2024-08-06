package com.example.userservice.exception;

public class BodyParameterMissingException extends RuntimeException{
    public BodyParameterMissingException(String message) {
        super(message);
    }
}
