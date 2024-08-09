package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureUploadRequest {
    private Long id;
    private String title;
    private String description;
    private int position;
    private Long length;
    private String value;
    private String type;
}
