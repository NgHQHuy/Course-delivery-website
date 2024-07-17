package com.example.registeredcourse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckRegisteredRequest {
    private Long userId;
    private Long courseId;
}
