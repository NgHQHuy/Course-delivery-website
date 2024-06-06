package com.example.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String type;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Chapter> chapters = new HashSet<>();

    public void setChapters(Set<Chapter> chapters) {
        this.chapters.addAll(chapters);
    }
}
