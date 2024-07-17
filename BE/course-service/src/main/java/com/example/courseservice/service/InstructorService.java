package com.example.courseservice.service;

import com.example.courseservice.entity.Instructor;
import com.example.courseservice.repository.InstructorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InstructorService {
    private InstructorRepository instructorRepository;

    public Optional<Instructor> findOne(Long instructorId) {
        return instructorRepository.findById(instructorId);
    }

    public Instructor saveState(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}
