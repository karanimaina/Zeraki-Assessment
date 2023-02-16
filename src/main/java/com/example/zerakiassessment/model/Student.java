package com.example.zerakiassessment.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "tb_student")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private  String name;
    @OneToOne
    private Course course;
    @ManyToOne
    private University university;
}
