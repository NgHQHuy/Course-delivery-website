package com.example.courseservice.repository;

import com.example.courseservice.entity.Chapter;
import com.example.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseId(Long courseId);
}
