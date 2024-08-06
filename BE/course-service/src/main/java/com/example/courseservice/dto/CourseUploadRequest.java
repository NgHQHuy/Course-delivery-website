package com.example.courseservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseUploadRequest {
    private Long id;

    @NotBlank(message = "Missing title")
    private String title;
    private String description;
    private String summary;
    private String requirements;
    @Positive(message = "Invalid price")
    private double price;
    @Positive(message = "Invalid instructor")
    private Long instructorId;
    private Set<Long> categoryIds;

    private List<SectionUploadRequest> sections;
//    private List<Agenda> agendas;

}
