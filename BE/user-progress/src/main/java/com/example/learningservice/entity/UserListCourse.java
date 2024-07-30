package com.example.learningservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class UserListCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long courseId;

    @CreationTimestamp
    private Timestamp addedAt;

    @ManyToOne
    @JoinColumn(name = "listId")
    private UserList list;
}
