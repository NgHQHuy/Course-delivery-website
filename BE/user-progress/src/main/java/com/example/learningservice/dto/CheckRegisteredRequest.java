package com.example.learningservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckRegisteredRequest {
    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;

    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;
}
