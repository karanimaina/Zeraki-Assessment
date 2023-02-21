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
     *
     * @param studentWrapper
     * takes in a StudentWrapper2 object that contains the necessary information to create a new student.
     * It then uses this information to first find the corresponding course and institution from their respective repositories.
     * If the student already exists (by admission number), an exception is thrown.
     * If the course is not found in the institution, another exception is thrown.
     * @return
     * If the student is new, a new Student object is created with the provided information along with the corresponding institution.
     * The new student is then saved to the student repository, and a StudentCourse object is created with the new student ID and the course ID, indicating that the student is enrolled in that course.
     * Finally, the StudentCourse object is saved to the student course repository.
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
 *The method retrieves the Student object from the repository based on the given id parameter, or throws a StudentException if the Student object is not found.
 *  It then deletes the Student object using the delete method of the studentRepository.
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
     *method that updates the name of a student with the given student ID.
     * The method takes in a StudentWrapper object that contains the new name and the ID of the student to be updated.
     * retrieves the student from the database using the provided student ID. If the student is not found, it throws a StudentException.
     *  sets the new name for the student and saves it to the database using the studentRepository.save() method.
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
     *
     * @param studentCourseWrapper
     * The method takes in a StudentCourseWrapper object that contains the studentId and the new courseId that the student will be assigned to.
     * uses the studentId to retrieve the corresponding Student object from the studentRepository. If the student does not exist, it throws a StudentException.
     * uses the new courseId to retrieve the corresponding Course object from the courseRepository. If the course does not exist, it throws a StudentException.
     * uses the courseId and studentId to retrieve the corresponding StudentCourse object from the studentCourseRepository. If the StudentCourse object does not exist, it means that no course was assigned to the student, so it throws a StudentException.
     * retrieves the Institution object associated with the student's current course using the getInstitution() method of the Student object and passes it to the institutionCourseRepository along with the new courseId to check if the new course is available in the same institution. If the course is not available, it throws a StudentException.
     * creates a new StudentCourse object with the new courseId and studentId and saves it using the studentCourseRepository.
     * deletes the old StudentCourse object using the studentCourseRepository.
     * @return  a UniversalResponse object
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
    /**
     * method that transfers a student from their current institution and course to a new institution and course.
     * @param studentWrapper
     *  takes a StudentWrapper object which contains the id of the student to be transferred, the institutionId of the new institution and the courseId of the new course.
     *  retrieves the Student entity from the database using the studentId from the StudentWrapper. It then retrieves the new Institution and Course entities using their respective IDs from the StudentWrapper. It checks if the new institution offers the new course by querying the institutionCourseRepository. If it doesn't, it throws a StudentException with an appropriate error message.
     *  deletes the student's current course (if any) by querying the studentCourseRepository and passing the courseId. It sets the student's Institution to the new institution and saves the Student entity to the database
     *  it creates a new StudentCourse entity with the student's id and the new courseId, and saves it to the database using studentCourseRepository.save().
     * @return
     */
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

    /**

     * @param studentWrapper
     * The method takes in a StudentWrapper object that contains the ID of the institution and the page number to return.
     * uses a Pageable object to specify the page number and the number of items to return.
     * retrieves the institution object using its ID and throws an exception if the institution is not found.
     *  calls the studentRepository to retrieve a Page object containing the students associated with the specified institution, using the findAllByInstitutionId method.
     *
     * @return  UniversalResponse object containing a status of 200
     */
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

    /**
     *
     * @param studentWrapper
     *  takes in a StudentPager object that contains the page number and the ID of the institution for which to retrieve the list of students.
     *  a Pageable object is created using the page number provided in the StudentPager
     *  The studentRepository is then called to retrieve the students that belong to the institution with the ID provided in the StudentPager, using the findAllByInstitutionId method.
     *  The retrieved students are then stored in a Page object
     * @return UniversalResponse object  with a status of 200
     */
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
