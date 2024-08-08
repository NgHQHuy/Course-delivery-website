package com.example.searchservice.dto;

import com.example.searchservice.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long userId;
    private String name;
    private RoleEnum role;
}
