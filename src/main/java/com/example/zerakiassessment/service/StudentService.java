package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.*;
import reactor.core.publisher.Mono;

public interface StudentService {

    Mono<UniversalResponse> addStudent(StudentWrapper2 studentWrapper);
    Mono<UniversalResponse> deleteStudent(long id);

    Mono<UniversalResponse> editStudent(StudentNameEdit studentWrapper);

    Mono<UniversalResponse> changeStudentCourse(StudentCourseWrapper studentWrapper);

    Mono<UniversalResponse> transferStudent(StudentWrapper studentWrapper);

    Mono<UniversalResponse> listStudents(StudentWrapper studentWrapper);

    Mono<UniversalResponse> findStudentsInInstitution(StudentPager studentWrapper);

}
