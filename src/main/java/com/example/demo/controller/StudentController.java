package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.entity.Student;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.payload.AuthenticationRequest;
import com.example.demo.payload.AuthenticationResponse;
import com.example.demo.service.StudentService;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents(){
        List <Student> result = studentService.getAllStudents();
        if(result.isEmpty()){
            throw new StudentNotFoundException();
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody  Student student){
        System.out.println(student);
        Student createdStudent = studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.OK);

    }
@PutMapping("{id}")
    public ResponseEntity<Student> updateStudentById(@RequestBody Student student,@PathVariable("id") Long studentId){
        Student updatedStudent = studentService.updateStudentById(studentId,student);
        return new ResponseEntity<>(updatedStudent,HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Student>> getStudentById(@PathVariable("id") Long studentId){
        Optional<Student> result = studentService.getStudentById(studentId);
        if(!result.isPresent()){
            throw new StudentNotFoundException();
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long studentId){
        studentService.deleteStudentById(studentId);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        UserDetails userDetails = userServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }
}
