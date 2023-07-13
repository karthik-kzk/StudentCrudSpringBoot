package com.example.demo.service.impl;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student createStudent(Student student){
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudentById(Long studentId, Student student){
        Student existingStudent = studentRepository.findById(studentId).get();
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartment(student.getDepartment());
        Student updateStudent = studentRepository.save(existingStudent);
        return updateStudent;
    }

    @Override
    public Optional<Student> getStudentById(Long studentId){
        Optional<Student> result = studentRepository.findById(studentId);
        return result;
    }

    @Override
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @Override
    public  void  deleteStudentById(Long studentId){

        studentRepository.deleteById(studentId);
    }

}
