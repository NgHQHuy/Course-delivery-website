package com.example.registeredcourse.exception;

public class BodyParameterMissingException extends RuntimeException{
    public BodyParameterMissingException(String message) {
        super(message);
    }
}
