package com.example.library.controller;

import com.example.library.entities.*;
import com.example.library.request_dtos.RequestBorrowRecordDto;
import com.example.library.response_dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.BorrowRecordRepository;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrowrecord")
public class BorrowRecordController {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final AuthorRepository authorRepository;

    public BorrowRecordController(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository, StudentRepository studentRepository, AuthorRepository authorRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.authorRepository = authorRepository;
    }
    @GetMapping
    public List<ResponseBorrowRecordDto> getAllBorrowRecords() {
        return borrowRecordRepository.findAll().stream().map(borrowRecord-> new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                        new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),new ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality())),
                        borrowRecord.getBorrowDate(),borrowRecord.getReturnDate()))
                        .collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBorrowRecordDto> getBorrowRecordById(@PathVariable Long id) {
        var borrowRecord= borrowRecordRepository.findById(id).orElse(null);
        if(borrowRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),new ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality())),
                borrowRecord.getBorrowDate(),borrowRecord.getReturnDate()));
    }
    @PostMapping
    public ResponseBorrowRecordDto createBorrowRecord(@RequestBody RequestBorrowRecordDto borrowRecord) {
        BorrowRecord borrowRecord1 = new BorrowRecord();
        String isbn = borrowRecord.getBook().getIsbn();
        String title = borrowRecord.getBook().getTitle();
        String author_name = borrowRecord.getBook().getAuthor().getName();
        String author_nationality = borrowRecord.getBook().getAuthor().getNationality();
        String student_name = borrowRecord.getStudent().getName();
        String student_email = borrowRecord.getStudent().getEmail();
        Author author = authorRepository.findByNameAndNationality(author_name, author_nationality).orElseGet(() -> authorRepository.save(new Author(null, author_name, author_nationality)));
        Book book = bookRepository.findByIsbn(isbn).orElseGet(() -> bookRepository.save(new Book(null, title, isbn, author)));
        Student student = studentRepository.findByNameAndEmail(student_name,student_email).orElseGet(() -> studentRepository.save(new Student(null,student_name,student_email)));
        borrowRecord1.setBook(book);
        borrowRecord1.setStudent(student);
        var borrowRecord2 = borrowRecordRepository.save(borrowRecord1);
        return new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord2.getStudent().getName(),borrowRecord2.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord2.getBook().getTitle(),borrowRecord2.getBook().getIsbn(),new ResponseAuthorDto(borrowRecord2.getBook().getAuthor().getName(),borrowRecord2.getBook().getAuthor().getNationality())),
                borrowRecord2.getBorrowDate(),borrowRecord2.getReturnDate());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBorrowRecordDto> updateBorrowRecord(@PathVariable Long id, @RequestBody RequestBorrowRecordDto borrowRecord) {
        BorrowRecord borrowRecord1 = new BorrowRecord();
        String isbn = borrowRecord.getBook().getIsbn();
        String title = borrowRecord.getBook().getTitle();
        String author_name = borrowRecord.getBook().getAuthor().getName();
        String author_nationality = borrowRecord.getBook().getAuthor().getNationality();
        String student_name = borrowRecord.getStudent().getName();
        String student_email = borrowRecord.getStudent().getEmail();
        Author author = authorRepository.findByNameAndNationality(author_name, author_nationality).orElseGet(() -> authorRepository.save(new Author(null, author_name, author_nationality)));
        Book book = bookRepository.findByIsbn(isbn).orElseGet(() -> bookRepository.save(new Book(null, title, isbn, author)));
        Student student = studentRepository.findByNameAndEmail(student_name,student_email).orElseGet(() -> studentRepository.save(new Student(null,student_name,student_email)));
        borrowRecord1.setBook(book);
        borrowRecord1.setStudent(student);
        var borrow= borrowRecordRepository.findById(id).map(borrowRecord2 -> {
            borrowRecord2.setStudent(borrowRecord1.getStudent());
            borrowRecord2.setBook(borrowRecord1.getBook());
            return borrowRecordRepository.save(borrowRecord2);
        }).orElse(null);
        if(borrow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrow.getStudent().getName(),borrow.getStudent().getEmail()),
                new ResponseBookDto(borrow.getBook().getTitle(),borrow.getBook().getIsbn(),new ResponseAuthorDto(borrow.getBook().getAuthor().getName(),borrow.getBook().getAuthor().getNationality())),
                borrow.getBorrowDate(),borrow.getReturnDate()));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        borrowRecordRepository.deleteById(id);
    }

}
