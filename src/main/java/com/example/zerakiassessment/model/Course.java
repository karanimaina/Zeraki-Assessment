package com.example.zerakiassessment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "tb_courses")
@Getter
@Setter
public class Course  {
    private String name;
    private String description;
    private int monthDuration;

}
