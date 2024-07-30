package com.example.learningservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AddManyCourseRequest {
    @NotNull(message = "Missing listId")
    @Positive(message = "Invalid listId")
    private Long listId;

    @NotNull(message = "Missing course list")
    private Set<Long> courses;
}
