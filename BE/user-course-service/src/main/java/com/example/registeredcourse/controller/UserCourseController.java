package com.example.registeredcourse.controller;

import com.example.registeredcourse.dto.CheckRegisteredRequest;
import com.example.registeredcourse.entity.UserCourse;
import com.example.registeredcourse.exception.SearchNotFoundException;
import com.example.registeredcourse.service.UserCourseService;
import com.example.registeredcourse.validate.UserCourseValidation;
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
    private static Logger logger = LoggerFactory.getLogger(UserCourseController.class);

    @PostMapping("add")
    public ResponseEntity<UserCourse> registerCourseForUser(@RequestBody UserCourse uc) {
        UserCourseValidation.validate(uc);
        if (!service.isValidCourse(uc.getCourseId())) throw new SearchNotFoundException("Course is not found");
        else if (!service.isValidUser(uc.getUserId())) throw new SearchNotFoundException("User is not found");

        UserCourse saved;
        if (service.isUserRegisteredCourse(uc.getUserId(), uc.getCourseId())) {
//            logger.info("Find out user has registered this course");
            saved = service.findOne(uc.getUserId(), uc.getCourseId());
        } else saved = service.save(uc);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<UserCourse>> getAllCourseByUser(@PathVariable Long id) {
        List<UserCourse> courseList = service.getAllByUserID(id);
        return ResponseEntity.ok(courseList);
    }

    @PostMapping("checkRegistered")
    public ResponseEntity<UserCourse> checkUserRegisteredCourse(@RequestBody CheckRegisteredRequest req) {
        if (!service.isValidCourse(req.getCourseId())) throw new SearchNotFoundException("Course is not found");
        else if (!service.isValidUser(req.getUserId())) throw new SearchNotFoundException("User is not found");

        if (service.isUserRegisteredCourse(req.getUserId(), req.getCourseId())) {
            return ResponseEntity.ok(service.findOne(req.getUserId(), req.getCourseId()));
        }
        return ResponseEntity.status(404).build();
    }
}
