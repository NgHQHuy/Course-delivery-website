package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String summary;
    private String requirements;
    private double price;
    private Long instructorId;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
