package com.example.ratingservice.repository;

import com.example.ratingservice.entity.AvgRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvgRatingRepository extends JpaRepository<AvgRating, Long> {
}
