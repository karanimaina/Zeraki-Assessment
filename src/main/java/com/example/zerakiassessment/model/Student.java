package com.example.zerakiassessment.model;

import jakarta.persistence.*;
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
public class Student  extends BaseEntity{    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private  String name;
    @OneToOne
    private Course course;
    @ManyToOne
    private University university;
}
