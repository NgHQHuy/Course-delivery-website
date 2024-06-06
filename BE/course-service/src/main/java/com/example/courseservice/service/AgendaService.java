package com.example.courseservice.service;

import com.example.courseservice.entity.Agenda;
import com.example.courseservice.repository.AgendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgendaService {

    private AgendaRepository repository;

    public Agenda save(Agenda data) {
        return repository.save(data);
    }

    public Agenda getOne(Long id) {
        if (repository.findById(id).isPresent()) return repository.findById(id).get();
        return null;
    }
}
