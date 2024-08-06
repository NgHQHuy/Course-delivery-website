package com.example.userservice.dto;

import com.example.userservice.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchResponse {
    private Long id;
    private String username;
    private String email;
    private RoleEnum role;
}
