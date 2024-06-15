package com.example.userprogress.controller;

import com.example.userprogress.dto.BaseResponse;
import com.example.userprogress.entity.UserProgress;
import com.example.userprogress.service.UserProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user-progress")
public class UserProgressController {
    @Autowired
    private UserProgressService userProgressService;

    @Operation(summary = "Thêm/sửa thông tin về tiến trình khóa học")
    @PostMapping
    public ResponseEntity<BaseResponse> saveProgress(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin tiến trình. Bổ sung id định danh cho các tiến trình để chỉnh sửa. Bỏ qua hoặc nhập sai id sẽ thêm mới vào CSDL.")
            @RequestBody UserProgress userProgress) {
        UserProgress savedProgress = userProgressService.save(userProgress);
        return ResponseEntity.ok(new BaseResponse(0, "Success"));
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
