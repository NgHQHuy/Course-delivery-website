package com.example.courseservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CourseNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private int totalLectures = 0;

    private Long length = 0L;

    private Long numOfStudent = 0L;
    private double rating = 0;
}
