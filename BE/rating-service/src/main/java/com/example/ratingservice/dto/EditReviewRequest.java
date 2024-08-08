package com.example.ratingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditReviewRequest {
    @NotNull(message = "Missing rating")
    @Positive(message = "Invalid rating")
    private double rating;
    @NotBlank(message = "Missing comment")
    private String comment;
}
