package com.example.courseservice.exception;

public class CourseAlreadyAddedException extends RuntimeException {
    public CourseAlreadyAddedException(String message) {
        super(message);
    }
}
