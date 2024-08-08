package com.example.searchservice.controller;

import com.example.searchservice.dto.*;
import com.example.searchservice.service.CategoryService;
import com.example.searchservice.service.CourseService;
import com.example.searchservice.service.InstructorService;
import com.example.searchservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/search")
@AllArgsConstructor
public class SearchController {
    private UserService userService;
    private InstructorService instructorService;
    private CourseService courseService;
    private CategoryService categoryService;

    @GetMapping("user")
    public ResponseEntity<List<User>> searchUser(@RequestParam("keyword") String keyword) {
        List<User> result = userService.search(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("instructor")
    public ResponseEntity<List<Instructor>> searchInstructor(@RequestParam("keyword") String keyword) {
        List<Instructor> result = instructorService.search(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("course")
    public ResponseEntity<List<Course>> searchCourse(@RequestParam("keyword") String keyword) {
        List<Course> result = courseService.search(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<OverviewResult> searchByAnything(@RequestParam("keyword") String keyword) {
        List<Course> courses = courseService.search(keyword);
        List<Category> categories = categoryService.search(keyword);
        List<Instructor> instructors = instructorService.search(keyword);
        OverviewResult result = new OverviewResult();
        result.setCourses(courses);
        result.setCategories(categories);
        result.setInstructors(instructors);
        return ResponseEntity.ok(result);
    }
}
