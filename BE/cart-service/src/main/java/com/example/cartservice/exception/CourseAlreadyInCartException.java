package com.example.cartservice.exception;

public class CourseAlreadyInCartException extends RuntimeException{
    public CourseAlreadyInCartException(String message) {
        super(message);
    }
}
