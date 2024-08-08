package com.example.learningservice.exception;

public class CourseAlreadyRegisteredException extends RuntimeException{
    public CourseAlreadyRegisteredException(String message) {
        super(message);
    }
}
