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
public class CourseUploadRequest {
    private Long id;
    private String name;
    private String description;

    private List<ChapterUploadRequest> chapters;
//    private List<Agenda> agendas;

}
