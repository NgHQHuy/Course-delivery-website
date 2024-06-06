package com.example.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "agendas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Agenda extends Course {
    private int position;
    private String link;

    @ManyToOne
    @JoinColumn(name = "chapterId", referencedColumnName = "id")
    private Chapter chapter;
}
