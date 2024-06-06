package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse {
    private int code;
    private String message;
}
