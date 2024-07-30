package com.example.courseservice.dto;

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
    private double length;
    private int totalLectures;
    private List<LectureUploadRequest> lectures;
}
