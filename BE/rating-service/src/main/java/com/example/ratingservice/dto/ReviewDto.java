package com.example.ratingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;
    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;

    @NotBlank(message = "Missing comment")
    private String comment;

    @NotNull(message = "Missing rating")
    @Positive(message = "Invalid rating")
    private double rating;
}
