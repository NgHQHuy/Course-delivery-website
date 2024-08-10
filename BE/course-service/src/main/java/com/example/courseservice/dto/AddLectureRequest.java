package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLectureRequest {
    @NotBlank(message = "Missing title")
    private String title;
    @NotNull(message = "Missing length")
    @Positive(message = "Invalid length")
    private Long length;
    @NotBlank(message = "Missing content type")
    private String type;
    @NotBlank(message = "Missing content value")
    private String value;
}
