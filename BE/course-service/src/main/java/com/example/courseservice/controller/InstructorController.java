package com.example.courseservice.controller;

import com.example.courseservice.entity.Instructor;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.service.InstructorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/instructor")
public class InstructorController {
    private InstructorService service;

    @GetMapping("{id}")
    public ResponseEntity<Instructor> getOneInstructor(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id).orElseThrow(() -> new SearchNotFoundException("Instructor is not found")));
    }

    @PostMapping("add")
    public ResponseEntity<Instructor> addInstructor(@RequestBody Instructor instructor) {
        Instructor savedInstructor = service.saveState(instructor);
        return ResponseEntity.ok(savedInstructor);
    }
}
