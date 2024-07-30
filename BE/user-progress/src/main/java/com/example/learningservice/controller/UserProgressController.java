package com.example.learningservice.controller;

import com.example.learningservice.dto.BaseResponse;
import com.example.learningservice.entity.UserProgress;
import com.example.learningservice.exception.CourseNotRegisteredException;
import com.example.learningservice.exception.SearchNotFoundException;
import com.example.learningservice.service.CourseService;
import com.example.learningservice.service.UserCourseService;
import com.example.learningservice.service.UserProgressService;
import com.example.learningservice.service.UserService;
import com.example.learningservice.validation.UserProgressValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user-progress")
public class UserProgressController {
    @Autowired
    private UserProgressService userProgressService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;
    private static Logger logger = LoggerFactory.getLogger(UserProgressController.class);

    @Operation(summary = "Thêm/sửa thông tin về tiến trình khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveProgress(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin tiến trình. Bổ sung id định danh cho các tiến trình để chỉnh sửa. Bỏ qua hoặc nhập sai id sẽ thêm mới vào CSDL.")
            @Valid @RequestBody UserProgress userProgress) {
        if (!courseService.isValidCourse(userProgress.getCourseId())) throw new SearchNotFoundException("Course is not found");
        else if (!userService.isValidUser(userProgress.getUserId())) throw new SearchNotFoundException("User is not found");


        boolean isRegistered = userCourseService.isUserRegisteredCourse(userProgress.getUserId(), userProgress.getCourseId());
        if (!isRegistered) {
            throw new CourseNotRegisteredException("User has not registered this course");
        }

        if (userProgress.getId() == null && userProgressService.isProgressAlreadyInDB(userProgress.getUserId(), userProgress.getCourseId())) {
            UserProgress inDB = userProgressService.findOne(userProgress.getUserId(), userProgress.getCourseId());
            userProgress.setId(inDB.getId());
        }
        UserProgress savedProgress = userProgressService.save(userProgress);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @Operation(summary = "Lấy toàn bộ thông tin về tiến trình học của người dùng")
    @GetMapping("{uid}")
    public ResponseEntity<List<UserProgress>> getAllProgressByUser(@Parameter(description = "ID của người dùng")
            @PathVariable Long uid) {
        List<UserProgress> userProgresses = userProgressService.getAllProgressByUser(uid);
        return ResponseEntity.ok(userProgresses);
    }

    @Operation(summary = "Lấy toàn bộ thông tin về tiến trình học của người dùng trên một khóa học")
    @GetMapping("{uid}/{courseId}")
    public ResponseEntity<List<UserProgress>> getAllProgressByUserForCourse(
            @Parameter(description = "ID của người dùng") @PathVariable("uid") Long uid,
            @Parameter(description = "ID khóa học") @PathVariable("courseId") Long courseId) {
        List<UserProgress> userProgresses = userProgressService.getAllProgressByUserForCourse(uid, courseId);
        return ResponseEntity.ok(userProgresses);
    }

}
