package com.example.courseservice.controller;

import com.example.courseservice.entity.Lecture;
import com.example.courseservice.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/agenda")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping("{id}/details")
    public ResponseEntity<Lecture> getOneAgenda(@PathVariable Long id) {
        Lecture lecture = lectureService.getOne(id);
        return ResponseEntity.ok(lecture);
    }
}
