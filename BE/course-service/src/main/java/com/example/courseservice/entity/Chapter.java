package com.example.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chapters")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chapter extends Course {
    private int position;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Agenda> agendas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "courseId", referencedColumnName = "id")
    private Course course;

    public void setAgendas(Set<Agenda> agendas) {
        this.agendas.addAll(agendas);
    }
}
