package com.example.courseservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SectionUploadRequest {
    private Long id;
    private String title;
    private String description;
    private int position;
    private List<LectureUploadRequest> lectures;
}
