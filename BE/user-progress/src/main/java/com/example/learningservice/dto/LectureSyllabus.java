package com.example.learningservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureSyllabus {
    private Long id;
    private String title;
    private String description;
    private int position;
}
