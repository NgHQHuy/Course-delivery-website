package com.example.learningservice.controller;

import com.example.learningservice.dto.CheckRegisteredRequest;
import com.example.learningservice.entity.UserCourse;
import com.example.learningservice.exception.CourseAlreadyRegisteredException;
import com.example.learningservice.exception.SearchNotFoundException;
import com.example.learningservice.service.CourseService;
import com.example.learningservice.service.UserCourseService;
import com.example.learningservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/user-course")
public class UserCourseController {

    private UserCourseService service;

    private CourseService courseService;
    private UserService userService;
    private static Logger logger = LoggerFactory.getLogger(UserCourseController.class);

    @PostMapping("add")
    public ResponseEntity<UserCourse> registerCourseForUser(@Valid @RequestBody UserCourse uc) {
        if (!courseService.isValidCourse(uc.getCourseId())) throw new SearchNotFoundException("Course is not found");
        else if (!userService.isValidUser(uc.getUserId())) throw new SearchNotFoundException("User is not found");

        UserCourse saved;
        if (service.isUserRegisteredCourse(uc.getUserId(), uc.getCourseId())) {
//            logger.info("Find out user has registered this course");
            throw new CourseAlreadyRegisteredException("User already registered this course");
        } else saved = service.save(uc);
        service.updateCourseNumOfStudent(uc.getCourseId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<UserCourse>> getAllCourseByUser(@PathVariable Long id) {
        List<UserCourse> courseList = service.getAllByUserID(id);
        return ResponseEntity.ok(courseList);
    }

    @PostMapping("checkRegistered")
    public ResponseEntity<UserCourse> checkUserRegisteredCourse(@Valid @RequestBody CheckRegisteredRequest req) {
        if (!courseService.isValidCourse(req.getCourseId())) throw new SearchNotFoundException("Course is not found");
        else if (!userService.isValidUser(req.getUserId())) throw new SearchNotFoundException("User is not found");

        if (service.isUserRegisteredCourse(req.getUserId(), req.getCourseId())) {
            return ResponseEntity.ok(service.findOne(req.getUserId(), req.getCourseId()));
        }
        return ResponseEntity.status(404).build();
    }
}
