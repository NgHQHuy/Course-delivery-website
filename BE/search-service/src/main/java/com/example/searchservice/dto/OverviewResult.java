package com.example.searchservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OverviewResult {
    private List<Course> courses;
    private List<Category> categories;
    private List<Instructor> instructors;
}
