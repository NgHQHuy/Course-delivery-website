package com.example.courseservice.repository;

import com.example.courseservice.entity.CourseNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseNumberRepository extends JpaRepository<CourseNumber, Long> {
}
