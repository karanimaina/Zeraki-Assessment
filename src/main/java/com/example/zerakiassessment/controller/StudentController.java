package com.example.zerakiassessment.controller;


import com.example.zerakiassessment.service.StudentService;
import com.example.zerakiassessment.wrapper.StudentWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/api/v1/student")
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/add")
    public Mono<ResponseEntity<UniversalResponse>> addStudent(@RequestBody StudentWrapper studentWrapper){
        return studentService.addStudent(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }
    @DeleteMapping("/")
    public Mono<ResponseEntity<UniversalResponse>> deleteStudent(@RequestBody StudentWrapper studentWrapper){
        return studentService.deleteStudent(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/edit")
    public Mono<ResponseEntity<UniversalResponse>> editStudent(@RequestBody StudentWrapper studentWrapper){
        return studentService.editStudent(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/change/course")
    public Mono<ResponseEntity<UniversalResponse>> changeStudentCourse(@RequestBody StudentWrapper studentWrapper){
        return studentService.changeStudentCourse(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/transfer")
    public Mono<ResponseEntity<UniversalResponse>> transferStudent(@RequestBody StudentWrapper student){
        return studentService.transferStudent(student)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @GetMapping("/")
    public Mono<ResponseEntity<UniversalResponse>> listStudentsInstitution(@RequestBody StudentWrapper student){
        return studentService.listStudents(student)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/institution")
    public Mono<ResponseEntity<UniversalResponse>>  findStudentsInInstitution(@RequestBody StudentWrapper studentWrapper){
        return studentService.findStudentsInInstitution(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

}
