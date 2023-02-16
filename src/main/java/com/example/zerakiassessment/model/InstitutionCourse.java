package com.example.zerakiassessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "tb_institution_course")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE tb_institution_course SET soft_delete=true where id=?")
@Where(clause = "soft_delete = false")
public class InstitutionCourse extends BaseEntity{
    private long institution;
    private long course;
}
