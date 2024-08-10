package com.example.ratingservice.exception;

import org.apache.catalina.User;

public class UserAlreadyReviewedException extends RuntimeException{
    public UserAlreadyReviewedException(String message) {
        super(message);
    }
}
