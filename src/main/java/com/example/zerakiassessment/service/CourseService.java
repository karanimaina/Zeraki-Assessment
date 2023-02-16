package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import reactor.core.publisher.Mono;

public interface CourseService {
    Mono<UniversalResponse> getCoursesByInstitution(CourseWrapper courseWrapper);

    Mono<UniversalResponse> filterCourseByName(CourseWrapper courseWrapper);

    Mono<UniversalResponse> getAllCourseWithNameSorted(boolean desc);

    Mono<UniversalResponse> deleteCourse(CourseWrapper courseWrapper);

    Mono<UniversalResponse> addCourseToInstitution(CourseWrapper courseWrapper);

    Mono<UniversalResponse> editCourse(CourseWrapper courseWrapper);
}
