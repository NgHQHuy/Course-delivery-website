package com.example.courseservice.service;

import com.example.courseservice.entity.Section;
import com.example.courseservice.repository.SectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SectionService {

    private SectionRepository repository;


    public Section save(Section data) {
        return repository.save(data);
    }

    public Section getOne(Long id) {
        if (repository.findById(id).isPresent()) return repository.findById(id).get();
        return null;
    }

    public List<Section> getByCourseId(Long courseId) {
        return repository.findByCourseId(courseId);
    }

    public void delete(Section s) {
        repository.delete(s);
    }
}
