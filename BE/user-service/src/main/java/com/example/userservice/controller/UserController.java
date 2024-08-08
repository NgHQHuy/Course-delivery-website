package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserProfile;
import static com.example.userservice.mapper.UserMapper.*;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("create")
    public ResponseEntity<BaseResponse> createUser(@RequestBody RegisterRequest request) {
        userService.addUser(request);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @PostMapping("profile/update")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody UserProfileDto profileDto) {
        UserProfile profile = userService.updateProfile(profileDto);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("profile")
    public ResponseEntity<UserProfile> getUserProfile(@RequestBody UsernameRequest request) {
        UserProfile profile = userService.getUserProfile(request.getUsername());
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Lấy thông tin của tất cả người dùng")
    @GetMapping
    public ResponseEntity<List<RegisterRequest>> getAll() {
        List<User> users = userService.getAll();
        List<RegisterRequest> userDtos = new ArrayList<>();
        for (User u : users) {
            userDtos.add(mapToUserDto(u));
        }
        return ResponseEntity.ok(userDtos);
    }

    @Operation(summary = "Tìm thông tin người dùng theo tài khoản")
    @PostMapping("findUsername")
    public ResponseEntity<User> findUsername(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin tài khoản mà người dùng cần tìm kiếm")
            @RequestBody UsernameRequest req) {
        User user = userService.findUsername(req.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Đổi mật khẩu")
    @PostMapping("changePassword")
    public ResponseEntity<BaseResponse> changePassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Thông tin liên quan đến việc đổi mật khẩu")
            @RequestBody ChangePasswordRequest req) {
        User user = userService.findUsername(req.getUsername());
        if (user == null) return new ResponseEntity<>(new BaseResponse("Account not found"), HttpStatus.NOT_FOUND);
        if (!user.getPassword().equals(req.getPassword())) {
            return new ResponseEntity<>(new BaseResponse("Password is mismatch"), HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(req.getNewPassword());
        userService.save(user);
        return new ResponseEntity<>(new BaseResponse("Success"), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUser(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    @GetMapping("search")
    public ResponseEntity<List<UserSearchResponse>> search(@RequestParam("keyword") String keyword) {
        List<UserProfile> result = userService.search(keyword);
        List<UserSearchResponse> responses = new ArrayList<>();
        for (UserProfile u : result) {
            Optional<User> optionalUser = userService.getUser(u.getUserId());
            User user = optionalUser.get();
            UserSearchResponse response = new UserSearchResponse();
            response.setUserId(u.getUserId());
            response.setName(u.getName());
            response.setRole(user.getRole().getName());
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("delete")
    public ResponseEntity<BaseResponse> deleteUser(@RequestParam("user") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }
}
