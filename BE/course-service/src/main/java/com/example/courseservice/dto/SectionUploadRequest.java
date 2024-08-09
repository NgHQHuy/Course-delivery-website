package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SectionUploadRequest {
    private Long id;

    @NotBlank(message = "Missing title")
    private String title;

    @NotBlank(message = "Missing description")
    private String description;

    @NotNull(message = "Missing position")
    @Positive(message = "Invalid position")
    private int position;
    private Set<LectureUploadRequest> lectures;
}
