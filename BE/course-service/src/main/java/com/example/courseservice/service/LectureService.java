package com.example.courseservice.service;

import com.example.courseservice.entity.Lecture;
import com.example.courseservice.entity.Section;
import com.example.courseservice.repository.LectureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LectureService {

    private LectureRepository repository;

    public Lecture save(Lecture data) {
        return repository.save(data);
    }

    public Lecture getOne(Long id) {
        if (repository.findById(id).isPresent()) return repository.findById(id).get();
        return null;
    }

    public void delete(Lecture l) {
        repository.deleteById(l.getId());
    }
}
