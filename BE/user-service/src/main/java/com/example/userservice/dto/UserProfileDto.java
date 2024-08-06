package com.example.userservice.dto;

import com.example.userservice.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private Long userId;
    private String name;
    private GenderEnum gender;
    private int age;
    private String phone;
    private String avatar;
}
