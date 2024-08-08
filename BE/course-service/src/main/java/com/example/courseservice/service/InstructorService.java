package com.example.courseservice.service;

import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.Instructor;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.repository.CourseRepository;
import com.example.courseservice.repository.InstructorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InstructorService {
    private InstructorRepository instructorRepository;
    private CourseRepository courseRepository;

    public Optional<Instructor> findOne(Long instructorId) {
        return instructorRepository.findById(instructorId);
    }

    public Instructor saveState(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public void delete(Long instructorId) {
        Optional<Instructor> optionalInstructor = findOne(instructorId);
        if (optionalInstructor.isEmpty()) throw new SearchNotFoundException("Instructor is not found");
        Instructor instructor = optionalInstructor.get();
        courseRepository.deleteAll(instructor.getCourses());
        instructorRepository.deleteById(instructorId);
    }

    public List<Instructor> search(String keyword) {
        return instructorRepository.search(keyword);
    }
}
