package com.example.learningservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateListRequest {
    @NotBlank(message = "Missing name")
    private String name;
    private String description;

    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;
}
