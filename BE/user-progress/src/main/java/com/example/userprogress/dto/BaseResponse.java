package com.example.userprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse {
    private int code;
    private String message;
}
