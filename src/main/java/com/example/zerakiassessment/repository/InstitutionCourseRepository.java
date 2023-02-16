package com.example.zerakiassessment.repository;

import com.example.zerakiassessment.model.InstitutionCourse;
import org.springframework.data.jpa.repository.JpaRepository;

;import java.util.List;
import java.util.Optional;

public interface InstitutionCourseRepository extends JpaRepository<InstitutionCourse,Long> {
    List<InstitutionCourse> findAllByInstitution(long courseId);

    Optional<InstitutionCourse> findByCourseAndInstitution(long courseId, long institutionId);

    boolean existsByInstitution(long institutionId);

    List<InstitutionCourse> findByCourse(long courseId);




}
