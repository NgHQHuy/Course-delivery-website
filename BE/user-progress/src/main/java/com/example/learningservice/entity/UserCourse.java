package com.example.learningservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;

    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;

    @CreationTimestamp
    private Timestamp createdAt;
}
