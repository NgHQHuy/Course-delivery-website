package com.example.courseservice.service;

import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepository courseRepository;

    public Course save(Course data) {
        return courseRepository.save(data);
    }


    public Course getOne(Long id) {
        if (courseRepository.findById(id).isPresent()) return courseRepository.findById(id).get();
        return null;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
