package com.example.courseservice.repository;

import com.example.courseservice.entity.Section;
import com.example.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
}
