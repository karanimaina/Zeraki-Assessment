package com.example.zerakiassessment.repository;


import com.example.zerakiassessment.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findTopByAdmissionNoEqualsIgnoreCase(String firstName);

    Page<Student> findAllByInstitutionId(long institutionId, Pageable pageable);
}
