package com.example.zerakiassessment.init;

import com.example.zerakiassessment.model.Course;
import com.example.zerakiassessment.model.Institution;
import com.example.zerakiassessment.model.InstitutionCourse;
import com.example.zerakiassessment.repository.CourseRepository;
import com.example.zerakiassessment.repository.InstitutionCourseRepository;
import com.example.zerakiassessment.repository.InstitutionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionCourseRepository institutionCourseRepository;
    @PostConstruct
    public void initDb(){
        createCourses();
        initInstitution();
        initInstitutionCourses();
        log.info("Initialized data successfully");
    }

    public void createCourses(){
        List<Course> courseList= List.of(
                Course.builder().name("Learning Java").description("Introduction to running java with springboot").monthDuration(48)
                        .build(),
                Course.builder().name("Learning Spring").description("Introduction to running spring").monthDuration(48)
                        .build(),
                Course.builder().name("Learning C++").description("Introduction to running c++").monthDuration(48)
                        .build()
        );
        courseRepository.saveAll(courseList);
    }

    public void initInstitution(){
        Institution institution= Institution.builder()
                .name("JKUAT")
                .telephone("524376723237")
                .location("Kiambu")
                .build();
        institutionRepository.save(institution);
    }

    public void initInstitutionCourses(){
        InstitutionCourse institutionCourse= InstitutionCourse.builder()
                .course(1)
                .institution(1).build();
        institutionCourseRepository.save(institutionCourse);
    }
}
