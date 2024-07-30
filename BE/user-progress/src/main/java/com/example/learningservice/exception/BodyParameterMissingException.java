package com.example.learningservice.exception;

public class BodyParameterMissingException extends RuntimeException{
    public BodyParameterMissingException(String message) {
        super(message);
    }
}
