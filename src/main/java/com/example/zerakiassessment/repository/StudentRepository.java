package com.example.zerakiassessment.repository;

import com.example.zerakiassessment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
