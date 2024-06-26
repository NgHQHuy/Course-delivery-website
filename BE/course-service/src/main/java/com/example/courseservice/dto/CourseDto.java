package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
