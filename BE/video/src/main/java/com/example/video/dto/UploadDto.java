package com.example.video.dto;

public class UploadDto extends BasicDto {
    private String id;

    public UploadDto(String id) {
        super(0);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
