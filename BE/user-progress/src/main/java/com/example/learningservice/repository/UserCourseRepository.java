package com.example.learningservice.repository;

import com.example.learningservice.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findAllByCourseId(Long courseId);
    List<UserCourse> findAllByUserId(Long userId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    UserCourse findByUserIdAndCourseId(Long userId, Long courseId);
}
