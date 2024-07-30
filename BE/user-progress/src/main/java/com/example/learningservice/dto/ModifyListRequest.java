package com.example.learningservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyListRequest {
    @NotNull(message = "Missing listId")
    @Positive(message = "Invalid listId")
    private Long listId;
    private String name;
    private String description;
}
