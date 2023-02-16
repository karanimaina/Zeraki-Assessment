package com.example.zerakiassessment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "tb_student")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE tb_student SET soft_delete=true where id=?")
@Where(clause = "soft_delete = false")
public class Student  extends BaseEntity{
    private String name;
    @Column(unique = true)
    private String admissionNo;
    @ManyToOne
    private Institution institution;
}
