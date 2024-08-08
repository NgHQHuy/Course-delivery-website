package com.example.searchservice.exception;

import com.example.searchservice.controller.SearchController;

public class SearchNotFoundException extends RuntimeException{
    public SearchNotFoundException(String message) {
        super(message);
    }
}
