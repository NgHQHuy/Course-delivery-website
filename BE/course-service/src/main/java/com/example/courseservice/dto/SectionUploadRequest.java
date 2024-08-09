package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SectionUploadRequest {
    private Long id;
    private String title;
    private String description;
    private int position;
    private Set<LectureUploadRequest> lectures;
}
