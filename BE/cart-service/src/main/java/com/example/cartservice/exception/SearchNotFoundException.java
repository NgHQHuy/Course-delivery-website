package com.example.cartservice.exception;

public class SearchNotFoundException extends RuntimeException{
    public SearchNotFoundException(String message) {
        super(message);
    }
}
