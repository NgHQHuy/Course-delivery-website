package com.example.userservice.mapper;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.User;

public class UserMapper {
    public static RegisterRequest mapToUserDto(User u) {
        RegisterRequest userDto = new RegisterRequest();
        userDto.setId(u.getId());
        userDto.setEmail(u.getEmail());
        userDto.setPassword(u.getPassword());
        userDto.setUsername(u.getUsername());
        userDto.setRole(u.getRole().getName());
        return userDto;
    }
}
