package com.example.ratingservice.repository;

import com.example.ratingservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Review> findAllByUserIdAndCourseId(Long userId, Long courseId);

    List<Review> findAllByUserId(Long userId);

    List<Review> findAllByCourseId(Long courseId);
}
