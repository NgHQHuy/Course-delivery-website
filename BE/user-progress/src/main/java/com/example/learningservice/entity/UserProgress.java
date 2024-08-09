package com.example.learningservice.entity;

import com.example.learningservice.enums.UserStatus;
import com.example.learningservice.validation.EnumNamePattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Missing userId")
    @Positive(message = "Invalid userId")
    private Long userId;

    @NotNull(message = "Missing courseId")
    @Positive(message = "Invalid courseId")
    private Long courseId;

    private Long sectionId;

    @NotNull(message = "Missing lectureId")
    @Positive(message = "Invalid lectureId")
    private Long lectureId;

    @NotNull(message = "Missing timestamp")
    private double timestamp;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Missing status")
    private UserStatus status;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
