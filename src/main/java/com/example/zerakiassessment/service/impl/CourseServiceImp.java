package com.example.zerakiassessment.service.impl;


import com.example.zerakiassessment.exceptions.CourseException;
import com.example.zerakiassessment.model.Course;
import com.example.zerakiassessment.model.Institution;
import com.example.zerakiassessment.model.InstitutionCourse;
import com.example.zerakiassessment.repository.CourseRepository;
import com.example.zerakiassessment.repository.InstitutionCourseRepository;
import com.example.zerakiassessment.repository.InstitutionRepository;
import com.example.zerakiassessment.repository.StudentCourseRepository;
import com.example.zerakiassessment.service.CourseService;
import com.example.zerakiassessment.wrapper.CourseWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final InstitutionCourseRepository institutionCourseRepository;
    private final InstitutionRepository institutionRepository;

    /**
     * retrieves the courses offered by an institution, given an institution ID
     * @param institutionId
     * t checks if the institution with the given ID exists in the database. If it doesn't exist, a CourseException is thrown.
     * If the institution exists, the code retrieves the list of institution-course mappings using institutionCourseRepository.findAllByInstitution(institutionId).
     * It then maps each mapping to its corresponding course using map(ic->courseRepository.findById(ic.getCourse())). If the course ID in the mapping is invalid, findById will return an empty optional.
     * The filter(Optional::isPresent) call filters out the empty optionals, leaving only the courses that exist in the database.
     * Finally, map(Optional::get) is used to extract the course objects from the optionals, and toList is used to collect the courses into a list.
     * @return Universal response  with a success status code (200)
     */
    @Override
    public Mono<UniversalResponse> getCoursesByInstitution(long institutionId) {
        return Mono.fromCallable(() -> {
            institutionRepository.findById(institutionId)
                    .orElseThrow(()-> new CourseException("Institution not found"));
            List<Course> courseList = institutionCourseRepository.findAllByInstitution(institutionId)
                    .stream()
                    .map(ic->courseRepository.findById(ic.getCourse()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return UniversalResponse.builder().status(200)
                    .message("Courses by Id").data(courseList).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * accepts a String name parameter which is used to search for courses that contain the provided name.
     * @param name
     *  call the findCourseByNameContainingIgnoreCase() method on the courseRepository object.
     * @return Universal response  with a success status code (200)
     */
    @Override
    public Mono<UniversalResponse> filterCourseByName(String name) {
        return Mono.fromCallable(() -> {
            List<Course> courseList = courseRepository
//                    .findCourseByNameContainingIgnoreCase(courseWrapper.name());
                    .findCourseByNameContainingIgnoreCase(name);

            return UniversalResponse.builder().status(200).message("Course List search result")
                    .data(courseList).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     *
     * @param desc
     * retrieves all the courses from the database and returns them sorted by name, either ascending or descending based on the desc parameter.
     * If desc is true, the courseRepository.findAllByOrderByNameDesc() method is called to retrieve the courses sorted in descending order
     * else if . If desc is false, the courseRepository.findAllByOrderByNameDesc() method is called to retrieve the courses sorted in ascending order
     * @return Universal response  with a success status code (200)
     */
    @Override
    public Mono<UniversalResponse> getAllCourseWithNameSorted(boolean desc) {
        return Mono.fromCallable(() -> {
            List<Course> courseList;
            if (desc) {
                courseList = courseRepository.findAllByOrderByNameDesc();
            } else {
                courseList = courseRepository.findAllByOrderByNameAsc();
            }
            return UniversalResponse.builder().status(200).message("Course list").data(courseList)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * @param courseWrapper
     * retrieves the Course entity from the database based on the provided courseId. If the course is not found, a CourseException is thrown.
     * method then checks if the course is already assigned to a student by checking if there are any entries in the StudentCourse table that reference the course. If so, a CourseException is thrown.
     * If the course is not assigned to any students, the course object is deleted from the courseRepository.
     * @return  UniversalResponse is returned with a status code of 200
     */
    @Override
    public Mono<UniversalResponse> deleteCourse(CourseWrapper courseWrapper) {
        return Mono.fromCallable(() -> {
            Course course = courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(() -> new CourseException("Course not found"));
            boolean existsByStudentCourse = studentCourseRepository.existsByCourseId(courseWrapper.courseId());
            if (existsByStudentCourse) {
                throw new CourseException(" Cannot delete course, Course is already assigned to a student");
            }
            courseRepository.delete(course);
            return UniversalResponse.builder().status(200).message("Course deleted successfully").build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * receives a CourseWrapper
     * @param courseWrapper
     *  retrieves a Course object from the courseRepository using the CourseWrapper's courseId, or throws a CourseException if no Course is found.
     *  retrieves an Institution object from the institutionRepository using the institutionId property of the CourseWrapper, or throws a CourseException if no Institution is found.
     *  checks if the given Course is already assigned to the given Institution by calling the findByCourseAndInstitution() method on the institutionCourseRepository. If the course is already assigned, the function throws a CourseException.
     *  If the course is not already assigned to the institution, the function creates a new InstitutionCourse object using the InstitutionCourse.builder() method and sets the institution and course properties to the IDs of the given Institution and Course, respectively.
     *  The new InstitutionCourse object is saved to the institutionCourseRepository using the save() method.
     * @return UniversalResponse object with a status of 200,
     */
    @Override
    public Mono<UniversalResponse> addCourseToInstitution(CourseWrapper courseWrapper) {
        return Mono.fromCallable(() -> {
           Course course = courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(() -> new CourseException("Course not found"));
           Institution institution = institutionRepository.findById(courseWrapper.institutionId())
                    .orElseThrow(() -> new CourseException("Institution not found"));
            institutionCourseRepository
                    .findByCourseAndInstitution(course.getId(), institution.getId())
                    .ifPresent(course1 -> {
                        throw new CourseException("Course is already assigned to institution");
                    });
            InstitutionCourse institutionCourse= InstitutionCourse.builder()
                    .institution(institution.getId()).course(course.getId()).build();

            InstitutionCourse saved=institutionCourseRepository.save(institutionCourse);
            return UniversalResponse.builder().status(200).message("Added course to Institution").data(saved)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * receives a CourseWrapper
     * @param courseWrapper
     * retrieves a Course object from the courseRepository using the CourseWrapper's courseId, or throws a CourseException if no Course is found.
     * checks if there's another Course with the same name as the new name provided in courseWrapper but with a different ID, using the courseRepository method findByNameEqualsIgnoreCaseAndIdNot(). If another Course is found, the function throws a CourseException.
     * If there are no errors, the function sets the new name in the Course object and saves it to the courseRepository
     * @return UniversalResponse object with a status of 200,
     */
    @Override
    public Mono<UniversalResponse> editCourse(CourseWrapper courseWrapper) {
        return Mono.fromCallable(()-> {
            Course course= courseRepository.findById(courseWrapper.courseId())
                    .orElseThrow(()->new CourseException("Course not found"));
            courseRepository.findByNameEqualsIgnoreCaseAndIdNot(courseWrapper.name(), course.getId())
                    .ifPresent(res-> {
                        throw new CourseException("Another course with same name exists");
                    });
            course.setName(courseWrapper.name());
            courseRepository.save(course);
            return UniversalResponse.builder().status(200).message("Course saved successfully").data(course).build();
        }).publishOn(Schedulers.boundedElastic());
    }
}
