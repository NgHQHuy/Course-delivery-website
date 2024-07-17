package com.example.userprogress.controller;

import com.example.userprogress.dto.BaseResponse;
import com.example.userprogress.entity.UserProgress;
import com.example.userprogress.exception.CourseNotRegisteredException;
import com.example.userprogress.exception.SearchNotFoundException;
import com.example.userprogress.service.UserProgressService;
import com.example.userprogress.validation.UserProgressValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private static Logger logger = LoggerFactory.getLogger(UserProgressController.class);

    @Operation(summary = "Thêm/sửa thông tin về tiến trình khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveProgress(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin tiến trình. Bổ sung id định danh cho các tiến trình để chỉnh sửa. Bỏ qua hoặc nhập sai id sẽ thêm mới vào CSDL.")
            @RequestBody UserProgress userProgress) {
        UserProgressValidation.validate(userProgress);
        if (!userProgressService.isValidUser(userProgress.getUserId())) {
            throw new SearchNotFoundException("User is not found");
        } else if (!userProgressService.isValidCourse(userProgress.getCourseId())) {
            throw new SearchNotFoundException("Course is not found");
        } else if (!userProgressService.isValidSection(userProgress.getSectionId())) {
            throw new SearchNotFoundException("Course's section is not found");
        } else if (!userProgressService.isValidLecture(userProgress.getLectureId())) {
            throw new SearchNotFoundException("Lecture is not found");
        }

        boolean isRegistered = userProgressService.isUserRegisteredCourse(userProgress.getUserId(), userProgress.getCourseId());
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
