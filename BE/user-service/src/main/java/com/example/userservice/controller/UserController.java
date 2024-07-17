package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponse;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.dto.FindUsernameRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Thêm/sửa thông tin người dùng")
    @PostMapping
    public ResponseEntity<User> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin người dùng. Bổ sung id của người dùng để chỉnh sửa thông tin. Nhập sai id người dùng hoặc bỏ qua id sẽ thêm mới vào CSDL.")
            @RequestBody UserDto u) {
        User user = userService.findUsername(u.getUsername());
        if (user == null) {
            user = new User();
            user.setUsername(u.getUsername());
        }
        user.setPassword(u.getPassword());
        user.setEmail(u.getEmail());
        user.setName(u.getName());
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "Lấy thông tin của tất cả người dùng")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Tìm thông tin người dùng theo tài khoản")
    @PostMapping("findUsername")
    public ResponseEntity<User> findUsername(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin tài khoản mà người dùng cần tìm kiếm")
            @RequestBody FindUsernameRequest req) {
        User user = userService.findUsername(req.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Đổi mật khẩu")
    @PostMapping("changePassword")
    public ResponseEntity<BaseResponse> changePassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin liên quan đến việc đổi mật khẩu")
            @RequestBody ChangePasswordRequest req) {
        User user = userService.findUsername(req.getUsername());
        if (user == null) return new ResponseEntity<>(new BaseResponse(-1, "Account not found"), HttpStatus.NOT_FOUND);
        if (!user.getPassword().equals(req.getPassword())) {
            return new ResponseEntity<>(new BaseResponse(-1, "Password is mismatch"), HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(req.getNewPassword());
        userService.save(user);
        return new ResponseEntity<>(new BaseResponse(0, "Success"), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUser(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }
}
