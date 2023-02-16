package com.example.zerakiassessment.repository;


import com.example.zerakiassessment.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourse,Long> {
    boolean existsByCourseId(long id);

    Optional<StudentCourse> findByCourseIdAndStudentId(long courseId, long studentId);

    Optional<StudentCourse> findByStudentId(long studentId);
}
