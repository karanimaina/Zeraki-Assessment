package com.example.zerakiassessment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "tb_institution")
@Getter
@Setter
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @OneToMany
    private List<Course> courses;
}
