package com.example.courseservice.exception;

public class BodyParameterMissingException extends RuntimeException{
    public BodyParameterMissingException(String message) {
        super(message);
    }
}
