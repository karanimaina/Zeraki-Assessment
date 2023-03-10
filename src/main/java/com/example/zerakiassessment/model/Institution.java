package com.example.zerakiassessment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
    @Column(unique = true)
    private String name;
    private String telephone;
    private String location;

}
