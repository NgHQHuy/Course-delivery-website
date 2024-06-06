package com.example.cartservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWithMessage extends BaseResponse{
    private String message;

    public ResponseWithMessage(int code, String message) {
        super(code);
        this.message = message;
    }
}
