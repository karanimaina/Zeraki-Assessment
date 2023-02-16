package com.example.zerakiassessment.service.impl;


import com.example.zerakiassessment.exceptions.CourseException;
import com.example.zerakiassessment.model.Course;
import com.example.zerakiassessment.model.InstitutionCourse;
import com.example.zerakiassessment.repository.CourseRepository;
import com.example.zerakiassessment.repository.InstitutionCourseRepository;
import com.example.zerakiassessment.repository.InstitutionRepository;
import com.example.zerakiassessment.repository.StudentCourseRepository;
import com.example.zerakiassessment.service.CourseService;
import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final InstitutionCourseRepository institutionCourseRepository;
    private final InstitutionRepository institutionRepository;

    /**
     *
     * @param courseWrapper Course Wrapper
     * @return Universal response or throw an error if operation fails
     */
    @Override
    public Mono<UniversalResponse> getCoursesByInstitution(CourseWrapper courseWrapper) {
        return Mono.fromCallable(() -> {
            Course course = courseRepository.findById(courseWrapper.courseId())
                    .orElse(null);
            if (course == null) {
                throw new CourseException("Course by Id does not exists");
            }
            long institution = courseWrapper.institutionId();
            List<Course> courseList = institutionCourseRepository.findAllByInstitution(institution)
                    .stream()
                    .map(ic->courseRepository.findById(ic.getCourse()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return UniversalResponse.builder().status(200)
                    .message("Courses by Id").data(courseList).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> filterCourseByName(CourseWrapper courseWrapper) {
        return Mono.fromCallable(() -> {
            List<Course> courseList = courseRepository
                    .findCourseByNameContainingIgnoreCase(courseWrapper.name());
            return UniversalResponse.builder().status(200).message("Course List search result")
                    .data(courseList).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> getAllCourseWithNameSorted(boolean desc) {
        return Mono.fromCallable(() -> {
            List<Course> courseList;
            if (desc) {
                courseList = courseRepository.findAllByOrderByNameDesc();
            } else {
                courseList = courseRepository.findAllByOrderByNameAsc();
            }
            return UniversalResponse.builder().status(200).message("Course list").data(courseList)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> deleteCourse(CourseWrapper courseWrapper) {
        return Mono.fromCallable(() -> {
            Course course = courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(() -> new CourseException("Course not found"));
            boolean existsByStudentCourse = studentCourseRepository.existsByCourseId(courseWrapper.courseId());
            if (existsByStudentCourse) {
                throw new CourseException(" Cannot delete course, Course is already assigned to a student");
            }
            courseRepository.delete(course);
            return UniversalResponse.builder().status(200).message("Course deleted successfully").build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> addCourseToInstitution(CourseWrapper courseWrapper) {
        //check if institution has course assigned
        return Mono.fromCallable(() -> {
            //check oif Course exists
            courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(() -> new CourseException("Course not found"));
            institutionRepository.findById(courseWrapper.institutionId())
                    .orElseThrow(() -> new CourseException("Institution not found"));
            institutionCourseRepository
                    .findByCourseAndInstitution(courseWrapper.courseId(), courseWrapper.institutionId())
                    .ifPresent(course1 -> {
                        throw new CourseException("Course is already assigned to institution");
                    });
            InstitutionCourse institutionCourse= InstitutionCourse.builder()
                    .institution(courseWrapper.institutionId()).course(courseWrapper.courseId()).build();

            InstitutionCourse saved=institutionCourseRepository.save(institutionCourse);
            return UniversalResponse.builder().status(200).message("Added course to Institution").data(saved)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> editCourse(CourseWrapper courseWrapper) {
        return Mono.fromCallable(()-> {
            Course course= courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(()->new CourseException("Course not found"));
            courseRepository.findByNameEqualsIgnoreCaseAndIdNot(courseWrapper.name(), course.getId())
                    .ifPresent(res-> {
                        throw new CourseException("Another course with same name exists");
                    });
            course.setName(courseWrapper.name());
            courseRepository.save(course);
            return UniversalResponse.builder().status(200).message("Course saved successfully").data(course).build();
        }).publishOn(Schedulers.boundedElastic());
    }
}
