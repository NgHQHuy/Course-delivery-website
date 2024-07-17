package com.example.userprogress.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
