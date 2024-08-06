package com.example.courseservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCategoryDto {
    @NotNull(message = "Missing categoryId")
    @Positive(message = "Invalid categoryId")
    private Long categoryId;
    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;
}
