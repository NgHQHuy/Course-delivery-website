package com.example.cartservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;

    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;
}
