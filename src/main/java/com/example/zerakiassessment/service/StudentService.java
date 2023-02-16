package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.StudentWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import reactor.core.publisher.Mono;

public interface StudentService {

    Mono<UniversalResponse> addStudent(StudentWrapper studentWrapper);
    Mono<UniversalResponse> deleteStudent(StudentWrapper studentWrapper);

    Mono<UniversalResponse> editStudent(StudentWrapper studentWrapper);

    Mono<UniversalResponse> changeStudentCourse(StudentWrapper studentWrapper);

    Mono<UniversalResponse> transferStudent(StudentWrapper studentWrapper);

    Mono<UniversalResponse> listStudents(StudentWrapper studentWrapper);

    Mono<UniversalResponse> findStudentsInInstitution(StudentWrapper studentWrapper);

}
