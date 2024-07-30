package com.example.learningservice.repository;

import com.example.learningservice.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findAllByUserId(Long id);

    List<UserProgress> findAllByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    UserProgress findByUserIdAndCourseId(Long userId, Long courseId);
}
