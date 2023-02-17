package com.example.zerakiassessment.service.impl;


import com.example.zerakiassessment.exceptions.InstitutionException;
import com.example.zerakiassessment.exceptions.StudentException;
import com.example.zerakiassessment.model.Course;
import com.example.zerakiassessment.model.Institution;
import com.example.zerakiassessment.model.Student;
import com.example.zerakiassessment.model.StudentCourse;
import com.example.zerakiassessment.repository.*;
import com.example.zerakiassessment.service.StudentService;
import com.example.zerakiassessment.wrapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final InstitutionCourseRepository institutionCourseRepository;
    private final InstitutionRepository institutionRepository;
/**
 * add student
 * set student to course
 * set student to institution
 */

    @Override
    public Mono<UniversalResponse> addStudent(StudentWrapper2 studentWrapper) {
        return Mono.fromCallable(() -> {
            Course course= courseRepository.findById(studentWrapper.courseId())
                            .orElseThrow(()-> new StudentException("Course not found"));

            Institution institution= institutionRepository.findById(studentWrapper.institutionId())
                            .orElseThrow(()-> new StudentException("Institution not found"));

            studentRepository.findTopByAdmissionNoEqualsIgnoreCase(studentWrapper.admissionNo())
                    .ifPresent(student -> {
                        throw new StudentException("Student by admission already exists");
                    });

            institutionCourseRepository.findByCourseAndInstitution(course.getId(),institution.getId())
                    .orElseThrow(()-> new StudentException("Course not found in institution"));

            Student student = Student
                    .builder()
                    .admissionNo(studentWrapper.admissionNo())
                    .name(studentWrapper.name())
                    .institution(institution)
                    .build();

            Student newstudent = studentRepository.save(student);
            StudentCourse studentCourse= StudentCourse.builder().courseId(course.getId())
                    .studentId(newstudent.getId()).build();
            studentCourseRepository.save(studentCourse);
            return UniversalResponse.builder().status(200).message("Student saved successfully").data(newstudent)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }
/**
 * check if student exist
 * delete student
 */

    @Override
    public Mono<UniversalResponse> deleteStudent(long id) {
        return Mono.fromCallable(() -> {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new StudentException("Student not found"));
            studentRepository.delete(student);
            return UniversalResponse.builder().status(200).message("Student deleted successfuly").build();
        }).publishOn(Schedulers.boundedElastic());
    }
    /**
     * find Student by admissionNo
     * Change  the  name of the Student
     */


    @Override
    public Mono<UniversalResponse> editStudent(StudentWrapper studentWrapper) {
        return Mono.fromCallable(() -> {
            Student student = studentRepository.findById(studentWrapper.studentId() )
                    .orElseThrow(() -> new StudentException("Student not found"));
            student.setName(studentWrapper.name());
            student = studentRepository.save(student);
            return UniversalResponse.builder().status(200).message("Student updated successfully")
                    .data(student).build();
        }).publishOn(Schedulers.boundedElastic());
    }

/**
 * find Student by id
 * find course by id
 * verify if the student has an assigned course
 * verify if the institution has the selected course
 * assign student the course
 */

    @Override
    public Mono<UniversalResponse> changeStudentCourse(StudentCourseWrapper studentCourseWrapper) {
        return Mono.fromCallable(() -> {
            //check if student is assigned a course
            Student student= studentRepository.findById(studentCourseWrapper.studentId())
                    .orElseThrow(()-> new StudentException("Student not found"));
            Course course= courseRepository.findById(studentCourseWrapper.courseId())
                    .orElseThrow(()->new StudentException("Course not found"));

            StudentCourse studentCourse= studentCourseRepository.findByCourseIdAndStudentId(course.getId(),student.getId())
                    .orElse(null);
            if(studentCourse==null){
                throw new StudentException("No course was assigned to student");
            }
             institutionCourseRepository
                    .findByCourseAndInstitution(course.getId(),student.getInstitution().getId())
                    .orElseThrow(()-> new StudentException("Course does not exist in institution"));
            StudentCourse newStudentCourse= StudentCourse.builder()
                    .courseId(course.getId()).studentId(student.getId()).build();
            studentCourseRepository.save(newStudentCourse);
            studentCourseRepository.delete(studentCourse);
            return UniversalResponse.builder().status(200).message("Student changed course successfully")
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> transferStudent(StudentWrapper studentWrapper) {
        return  Mono.fromCallable(()-> {
            Student  student= studentRepository.findById(studentWrapper.studentId())
                    .orElseThrow(()-> new StudentException("Student not found"));
            Institution institution= institutionRepository.findById(studentWrapper.institutionId())
                    .orElseThrow(()-> new StudentException("Institution not found"));
            Course course=  courseRepository.findById(studentWrapper.courseId())
                    .orElseThrow(()->  new StudentException("Course not found"));
            institutionCourseRepository.findByCourseAndInstitution(course.getId(), institution.getId())
                    .orElseThrow(()-> new StudentException("Institution does not offer course"));

            studentCourseRepository.findByStudentId(course.getId()).ifPresent(studentCourseRepository::delete);
            student.setInstitution(institution);
            studentRepository.save(student);
            StudentCourse newStudentCourse= StudentCourse.builder()
                    .studentId(student.getId())
                    .courseId(course.getId())
                    .build();
            studentCourseRepository.save(newStudentCourse);
            return UniversalResponse.builder().status(200).message("Student transferred successfully").build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> listStudents(StudentWrapper studentWrapper) {
        return Mono.fromCallable(()-> {
            Pageable pageable= PageRequest.of(studentWrapper.pageId(),10);
            institutionRepository.findById(studentWrapper.institutionId())
                    .orElseThrow(()-> new InstitutionException("Institution not found"));
            Page<Student> students= studentRepository.findAllByInstitutionId(studentWrapper.institutionId(), pageable);
            return UniversalResponse.builder().status(200).message("Student list").data(students)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> findStudentsInInstitution(StudentPager studentWrapper) {
        return Mono.fromCallable(()-> {
            Pageable pageable= PageRequest.of(studentWrapper.page(),10);
            Page<Student> student= studentRepository.findAllByInstitutionId(studentWrapper.institutionId(),pageable);
            return UniversalResponse.builder().status(200)
                    .message("Student list").data(student).build();
        }).publishOn(Schedulers.boundedElastic());
    }
}
