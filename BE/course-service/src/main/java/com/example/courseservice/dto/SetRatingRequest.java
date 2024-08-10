package com.example.courseservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetRatingRequest {
    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;
    @NotNull(message = "Missing rating")
    @PositiveOrZero(message = "Missing rating")
    private double rating;
}
