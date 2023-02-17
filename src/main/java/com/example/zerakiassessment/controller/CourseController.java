package com.example.zerakiassessment.controller;


import com.example.zerakiassessment.service.CourseService;
import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.CourseWrapper2;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/api/v1/course")
@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @GetMapping("/institution/all")
    public Mono<ResponseEntity<UniversalResponse>> getCoursesByInstitution(@RequestParam long institutionId ){
        return courseService.getCoursesByInstitution(institutionId)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }
    @PostMapping("/add/institution")
    public Mono<ResponseEntity<UniversalResponse>>addCourseToInstitution( @RequestBody CourseWrapper courseWrapper){
        return courseService.addCourseToInstitution(courseWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @GetMapping("/filter/name")
    public Mono<ResponseEntity<UniversalResponse>> filterCourseByName(@RequestParam  String  name){
        return courseService.filterCourseByName(name)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<UniversalResponse>> getCourses(@RequestParam(required = false) boolean desc){
        return courseService.getAllCourseWithNameSorted(desc)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteCourse(@RequestBody CourseWrapper courseWrapper){
        return courseService.deleteCourse(courseWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/edit")
    public Mono<ResponseEntity<UniversalResponse>> editCourse(@RequestBody CourseWrapper courseWrapper){
        return courseService.editCourse(courseWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

}
