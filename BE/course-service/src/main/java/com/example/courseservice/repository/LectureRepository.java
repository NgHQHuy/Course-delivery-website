package com.example.courseservice.repository;

import com.example.courseservice.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findBySectionId(Long sectionId);
}
