package com.example.zerakiassessment.services;

import com.example.zerakiassessment.dto.StudentDto;
import com.example.zerakiassessment.model.Student;
import com.example.zerakiassessment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService  {
    private final StudentRepository studentRepository;

    public Student addStudent(StudentDto studentDto){
        Student student = studentRepository.findByName(studentDto.getName());
        if (student != null){
            throw  new StudentExist("Student exist")
        }
    }

}
