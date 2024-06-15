package com.example.video.dto;

public class ResponseDto extends BasicDto {
    private int code;
    private String message;

    public ResponseDto(int code, String message) {
        super(code);
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        super.setCode(code);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
