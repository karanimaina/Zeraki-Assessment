package com.example.zerakiassessment.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Table(name = "tb_institution")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE tb_institution SET soft_delete=true where id=?")
@Where(clause = "soft_delete = false")
public class Institution extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @OneToMany
    private List<Course> courses;
}
