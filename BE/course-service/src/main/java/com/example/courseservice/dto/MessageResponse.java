package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends BaseResponse {
    private String message;

    public MessageResponse(int code, String message) {
        super(code);
        this.message = message;
    }
}
