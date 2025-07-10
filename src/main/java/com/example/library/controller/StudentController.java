package com.example.library.controller;


import com.example.library.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseStudentDto;
import com.example.library.request_dtos.RequestStudentDto;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
       this.studentService = studentService;
    }
    @GetMapping
    public List<ResponseStudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseStudentDto> getStudentById(@PathVariable Long id) {
        var student= studentService.getStudentById(id);
        if(student == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseStudentDto(student.getName(),student.getEmail()));
    }
    @PostMapping
    public ResponseStudentDto createStudent(@RequestBody RequestStudentDto student) {
        return studentService.createStudent(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseStudentDto> updateStudent(@PathVariable Long id, @RequestBody RequestStudentDto student) {
        var student2= studentService.getStudentById(id);
        if(student2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseStudentDto(student.getName(),student.getEmail()));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
    }

}
