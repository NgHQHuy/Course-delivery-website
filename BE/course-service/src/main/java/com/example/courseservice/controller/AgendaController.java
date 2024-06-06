package com.example.courseservice.controller;

import com.example.courseservice.entity.Agenda;
import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.AgendaRepository;
import com.example.courseservice.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping("{id}/details")
    public ResponseEntity<Agenda> getOneAgenda(@PathVariable Long id) {
        Agenda agenda = agendaService.getOne(id);
        return ResponseEntity.ok(agenda);
    }
}
