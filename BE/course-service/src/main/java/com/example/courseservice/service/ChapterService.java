package com.example.courseservice.service;

import com.example.courseservice.entity.Chapter;
import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.ChapterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChapterService{

    private ChapterRepository repository;


    public Chapter save(Chapter data) {
        return repository.save(data);
    }

    public Chapter getOne(Long id) {
        if (repository.findById(id).isPresent()) return repository.findById(id).get();
        return null;
    }

    public List<Chapter> getByCourseId(Long courseId) {
        return repository.findByCourseId(courseId);
    }
}
