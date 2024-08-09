package com.example.courseservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InstructorDto {
    private Long id;

    @NotBlank(message = "Missing name")
    private String name;

    private String avatar;

    private List<Long> courses = new ArrayList<>();

    public void setCourses(List<Long> courses) {
        this.courses.addAll(courses);
    }
}
