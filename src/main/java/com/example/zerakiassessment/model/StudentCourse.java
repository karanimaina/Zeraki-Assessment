package com.example.zerakiassessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "tb_student_course")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE tb_student_course SET soft_delete=true where id=?")
@Where(clause = "soft_delete = false")
public class StudentCourse  extends BaseEntity{
    private long studentId;
    private long courseId;
}
