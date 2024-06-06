package com.example.userprogress.repository;

import com.example.userprogress.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findAllByUserId(Long id);
}
