package com.example.learningservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ListCourseDto {
    private Long id;
    private Long courseId;
    private Long listId;
    private Timestamp addedAt;
}
