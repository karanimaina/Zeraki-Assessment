package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.CourseInstitutionWrapper;
import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.CourseWrapper2;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import reactor.core.publisher.Mono;

public interface CourseService {
    Mono<UniversalResponse> getCoursesByInstitution(CourseInstitutionWrapper courseWrapper);

//    Mono<UniversalResponse> filterCourseByName(CourseWrapper courseWrapper);
    Mono<UniversalResponse> filterCourseByName(String name);

    Mono<UniversalResponse> getAllCourseWithNameSorted(boolean desc);

    Mono<UniversalResponse> deleteCourse(CourseWrapper courseWrapper);

    Mono<UniversalResponse> addCourseToInstitution(CourseWrapper2 courseWrapper);

    Mono<UniversalResponse> editCourse(CourseWrapper courseWrapper);
}
