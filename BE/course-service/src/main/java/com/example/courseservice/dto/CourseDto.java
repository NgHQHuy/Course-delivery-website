package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
public class CourseDto {
    @NotNull(message = "Missing id")
    @Positive(message = "Invalid id")
    private Long id;
    @NotBlank(message = "Missing title")
    private String title;
    @NotBlank(message = "Missing description")
    private String description;
    @NotBlank(message = "Missing summary")
    private String summary;
    @NotBlank(message = "Missing requirement")
    private String requirements;
    @NotNull(message = "Missing price")
    @PositiveOrZero(message = "Invalid instructorId")
    private double price;
    @NotNull(message = "Missing instructorId")
    @Positive(message = "Invalid instructorId")
    private Long instructorId;
    @NotBlank(message = "Missing thumbnail")
    private String thumbnail;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
