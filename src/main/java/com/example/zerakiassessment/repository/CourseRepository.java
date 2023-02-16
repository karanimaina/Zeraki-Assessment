package com.example.zerakiassessment.repository;

import com.example.zerakiassessment.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
