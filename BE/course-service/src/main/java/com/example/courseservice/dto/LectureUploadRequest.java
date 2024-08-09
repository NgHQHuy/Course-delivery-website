package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureUploadRequest {
    private Long id;

    @NotBlank(message = "Missing title")
    private String title;
    @NotBlank(message = "Missing description")
    private String description;
    @NotNull(message = "Missing position")
    @Positive(message = "Invalid position")
    private int position;
    @NotNull(message = "Missing length")
    @Positive(message = "Invalid length")
    private Long length;
    @NotBlank(message = "Missing value")
    private String value;
    @NotBlank(message = "Missing type")
    private String type;
}
