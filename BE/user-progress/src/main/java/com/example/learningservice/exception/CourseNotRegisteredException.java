package com.example.learningservice.exception;

public class CourseNotRegisteredException extends RuntimeException{
    public CourseNotRegisteredException(String message) {
        super(message);
    }
}
