package com.example.ratingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class AvgRating {
    @Id
    private Long courseId;
    private double avgRating;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "courseId", referencedColumnName = "courseId", updatable = false, insertable = false)
    private Set<Review> reviews = new HashSet<>();
}
