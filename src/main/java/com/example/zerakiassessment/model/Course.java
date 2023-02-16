package com.example.zerakiassessment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "tb_courses")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {
    private String name;
    private String description;
    private int monthDuration;

}
