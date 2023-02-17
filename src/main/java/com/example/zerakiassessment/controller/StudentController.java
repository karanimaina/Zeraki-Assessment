package com.example.zerakiassessment.controller;


import com.example.zerakiassessment.service.StudentService;
import com.example.zerakiassessment.wrapper.*;
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
    public Mono<ResponseEntity<UniversalResponse>> addStudent(@RequestBody StudentWrapper2 studentWrapper){
        return studentService.addStudent(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }
    @DeleteMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteStudent(@RequestBody long id){
        return studentService.deleteStudent(id)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/edit")
    public Mono<ResponseEntity<UniversalResponse>> editStudent(@RequestBody StudentNameEdit studentWrapper){
        return studentService.editStudent(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/change/course")
    public Mono<ResponseEntity<UniversalResponse>> changeStudentCourse(@RequestBody StudentCourseWrapper studentWrapper){
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

    @GetMapping("/student/institutions")
    public Mono<ResponseEntity<UniversalResponse>> listStudentsInstitution(@RequestBody StudentWrapper student){
        return studentService.listStudents(student)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/institution")
    public Mono<ResponseEntity<UniversalResponse>>  findStudentsInInstitution(@RequestBody StudentPager studentWrapper){
        return studentService.findStudentsInInstitution(studentWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

}
