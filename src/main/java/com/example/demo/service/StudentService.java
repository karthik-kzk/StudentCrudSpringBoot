package com.example.demo.service;

import com.example.demo.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {


    default   List<Student> getAllStudents(){
        return null;
    };



    default  Student createStudent(Student student){
        return student;
    };

    Student updateStudentById(Long studentId, Student student);

    Optional<Student> getStudentById(Long studentId);

    void  deleteStudentById(Long studentId);
}
