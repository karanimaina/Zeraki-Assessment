package com.example.zerakiassessment.repository;


import com.example.zerakiassessment.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCourseByNameContainingIgnoreCase(String courseName);

    List<Course> findAllByOrderByNameAsc();

    List<Course> findAllByOrderByNameDesc();
    Optional<Course> findByName(String name);

    Optional<Course> findByNameEqualsIgnoreCaseAndIdNot(String name, long id);
}
