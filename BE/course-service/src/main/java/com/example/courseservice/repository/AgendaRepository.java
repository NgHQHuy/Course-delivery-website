package com.example.courseservice.repository;

import com.example.courseservice.entity.Agenda;
import com.example.courseservice.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByChapterId(Long chapterId);
}
