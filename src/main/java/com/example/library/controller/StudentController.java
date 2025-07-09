package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Student;
import com.example.library.repository.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseStudentDto;
import com.example.library.request_dtos.RequestStudentDto;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @GetMapping
    public List<ResponseStudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map(student -> new ResponseStudentDto(student.getName(),student.getEmail())).collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseStudentDto> getStudentById(@PathVariable Long id) {
        var student= studentRepository.findById(id).orElse(null);
        if(student == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseStudentDto(student.getName(),student.getEmail()));
    }
    @PostMapping
    public ResponseStudentDto createStudent(@RequestBody RequestStudentDto student) {
        Student student1 = new Student();
        student1.setName(student.getName());
        student1.setEmail(student.getEmail());
        Student student2= studentRepository.save(student1);
        return new ResponseStudentDto(student2.getName(), student2.getEmail());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseStudentDto> updateStudent(@PathVariable Long id, @RequestBody RequestStudentDto student) {
        var student2= studentRepository.findById(id).map(student1 -> {
            student1.setName(student.getName());
            student1.setEmail(student.getEmail());
            return studentRepository.save(student1);
        }).orElse(null);
        if(student2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseStudentDto(student.getName(),student.getEmail()));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

}
