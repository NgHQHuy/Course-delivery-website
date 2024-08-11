package com.example.searchservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Course {
    private Long courseId;
    private String title;
    private String description;
    private String summary;
    private String requirements;
    private double price;
    private Long instructorId;
    private int totalLectures;
    private Long length;
    private Long numOfStudent;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
