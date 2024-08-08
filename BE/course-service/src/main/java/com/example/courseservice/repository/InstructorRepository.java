package com.example.courseservice.repository;

import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("SELECT i FROM Instructor i WHERE i.name LIKE %?1%")
    List<Instructor> search(String keyword);
}
