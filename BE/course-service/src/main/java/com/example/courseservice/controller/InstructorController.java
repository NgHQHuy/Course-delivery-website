package com.example.courseservice.controller;

import com.example.courseservice.dto.InstructorDto;
import com.example.courseservice.entity.Course;
import com.example.courseservice.entity.Instructor;
import com.example.courseservice.exception.BodyParameterMissingException;
import com.example.courseservice.exception.SearchNotFoundException;
import com.example.courseservice.service.CourseService;
import com.example.courseservice.service.InstructorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("api/instructor")
public class InstructorController {
    private InstructorService service;
    private CourseService courseService;

    @GetMapping("{id}")
    public ResponseEntity<InstructorDto> getOneInstructor(@PathVariable Long id) {
        Instructor instructor = service.findOne(id).orElseThrow(() -> new SearchNotFoundException("Instructor is not found"));
        return ResponseEntity.ok(mapToInstructorDto(instructor));
    }

    @PostMapping("add")
    public ResponseEntity<InstructorDto> addInstructor(@Valid @RequestBody InstructorDto instructor) {
        Instructor savedInstructor = service.saveState(mapToInstructorEntity(instructor));
        return ResponseEntity.ok(mapToInstructorDto(savedInstructor));
    }

    @PostMapping("update")
    public ResponseEntity<InstructorDto> updateInstructor(@Valid @RequestBody InstructorDto instructorDto) {

        if (instructorDto.getId() == null) throw new BodyParameterMissingException("Missing id");
        Instructor savedInstructor = service.saveState(mapToInstructorEntity(instructorDto));
        return ResponseEntity.ok(mapToInstructorDto(savedInstructor));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteInstructor(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(200).build();
    }

    private InstructorDto mapToInstructorDto(Instructor i) {
        InstructorDto dto = new InstructorDto();
        dto.setId(i.getId());
        dto.setName(i.getName());
        dto.setDescription(i.getDescription());
        List<Long> courses = new ArrayList<>();
        for (Course course : i.getCourses()) {
            courses.add(course.getId());
        }
        dto.setCourses(courses);
        return dto;
    }

    private Instructor mapToInstructorEntity(InstructorDto idto) {
        Instructor instructor = new Instructor();
        instructor.setId(idto.getId());
        instructor.setDescription(idto.getDescription());
        instructor.setName(idto.getName());
        Set<Course> courses = new HashSet<>();

        for (Long courseId : idto.getCourses()) {
            Course course = courseService.getOne(courseId);
            if (course == null) {
                throw new SearchNotFoundException("Course not found");
            }
            courses.add(course);
        }
        instructor.setCourses(courses);
        return instructor;
    }
}
