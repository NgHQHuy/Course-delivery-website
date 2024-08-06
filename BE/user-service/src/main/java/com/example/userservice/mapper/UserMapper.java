package com.example.userservice.mapper;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User u) {
        UserDto userDto = new UserDto();
        userDto.setId(u.getId());
        userDto.setEmail(u.getEmail());
        userDto.setPassword(u.getPassword());
        userDto.setUsername(u.getUsername());
        userDto.setRole(u.getRole().getName());
        return userDto;
    }
}
