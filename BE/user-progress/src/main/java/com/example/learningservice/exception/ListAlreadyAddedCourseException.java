package com.example.learningservice.exception;

public class ListAlreadyAddedCourseException extends RuntimeException{
    public ListAlreadyAddedCourseException(String message) {
        super(message);
    }
}
