package com.example.courseservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectionSyllabus {
    private Long id;
    private String title;
    private int position;
    private List<LectureSyllabus> lectures;
}
