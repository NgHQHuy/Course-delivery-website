package com.example.userservice.exception;

public class AuthErrorException extends RuntimeException{
    public AuthErrorException(String message) {
        super(message);
    }
}
