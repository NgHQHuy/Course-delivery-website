package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.User;
import com.example.userservice.service.AuthorizationService;
import com.example.userservice.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.userservice.mapper.UserMapper.mapToUserDto;

@RestController
@RequestMapping("public/auth")
@AllArgsConstructor
public class AuthorizationController {
    private AuthorizationService authorizationService;
    private JwtService jwtService;

    @PostMapping("register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest u) {
        authorizationService.addUser(u);
        return ResponseEntity.ok(new BaseResponse("Success"));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User authenticatedUser = authorizationService.login(request);

        String token = jwtService.generateToken(authenticatedUser);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpires(jwtService.getExpiration());
        return ResponseEntity.ok(response);
    }
}
