package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Student;
import com.example.library.repository.StudentRepository;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        var student= studentRepository.findById(id).orElse(null);
        if(student == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        var student2= studentRepository.findById(id).map(student1 -> {
            student1.setName(student.getName());
            student1.setEmail(student.getEmail());
            return studentRepository.save(student1);
        }).orElse(null);
        if(student2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student2);
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

}
