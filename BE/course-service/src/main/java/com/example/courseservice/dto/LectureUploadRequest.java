package com.example.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LectureUploadRequest {
    private Long id;
    private String title;
    private String description;
    private String link;
    private int position;
    private double length;
    private String value;
    private String type;
}
