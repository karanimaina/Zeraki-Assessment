package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.CourseWrapper2;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import reactor.core.publisher.Mono;

public interface CourseService {
    Mono<UniversalResponse> getCoursesByInstitution(long institutionId);

//    Mono<UniversalResponse> filterCourseByName(CourseWrapper courseWrapper);
    Mono<UniversalResponse> filterCourseByName(String name);

    Mono<UniversalResponse> getAllCourseWithNameSorted(boolean desc);

    Mono<UniversalResponse> deleteCourse(CourseWrapper courseWrapper);

    Mono<UniversalResponse> addCourseToInstitution(CourseWrapper courseWrapper);

    Mono<UniversalResponse> editCourse(CourseWrapper courseWrapper);
}
