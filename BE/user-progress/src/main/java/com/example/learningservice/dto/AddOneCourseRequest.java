package com.example.learningservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOneCourseRequest {
    @NotNull(message = "Missing listId")
    @Positive(message = "Invalid listId")
    private Long listId;

    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;
}
