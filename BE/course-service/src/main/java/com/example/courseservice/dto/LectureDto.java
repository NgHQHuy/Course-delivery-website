package com.example.courseservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LectureDto {
    @Positive(message = "Invalid lecture id")
    @NotNull(message = "Lecture id is mandatory")
    private Long id;
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int position;
    private String type;
    private Long length;
    private String value;
    @Positive(message = "Invalid section id")
    @NotNull(message = "Section id is mandatory")
    private Long sectionId;
}
