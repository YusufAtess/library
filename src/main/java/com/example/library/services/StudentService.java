package com.example.library.services;

import com.example.library.repository.BorrowRecordRepository;

import com.example.library.entities.Student;
import com.example.library.repository.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseStudentDto;
import com.example.library.request_dtos.RequestStudentDto;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    public StudentService(StudentRepository studentRepository, BorrowRecordRepository borrowRecordRepository) {
        this.studentRepository = studentRepository;
        this.borrowRecordRepository=borrowRecordRepository;
    }

    public List<ResponseStudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map(student -> new ResponseStudentDto(student.getName(),student.getEmail())).collect(Collectors.toList());
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public ResponseStudentDto createStudent(RequestStudentDto student) {
        Student student1 = new Student();
        student1.setName(student.getName());
        student1.setEmail(student.getEmail());
        Student student2= studentRepository.save(student1);
        return new ResponseStudentDto(student2.getName(), student2.getEmail());
    }


    public Student updateStudent(Long id, RequestStudentDto student) {
        return studentRepository.findById(id).map(student1 -> {
            student1.setName(student.getName());
            student1.setEmail(student.getEmail());
            return studentRepository.save(student1);
        }).orElse(null);

    }

    public void deleteStudentById(Long id) {
        borrowRecordRepository.findByStudent_Id(id).forEach(borrowRecord -> borrowRecordRepository.deleteById(borrowRecord.getId()));
        studentRepository.deleteById(id);
    }

}

