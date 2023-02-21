package com.example.zerakiassessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "tb_courses")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE tb_courses SET soft_delete=true where id=?")
@Where(clause = "soft_delete = false")
public class Course extends BaseEntity{
    private String name;
    private String description;
    private int monthDuration;

}
