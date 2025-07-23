package com.example.library.controller;


import com.example.library.request_dtos.RequestBorrowRecordDto;
import com.example.library.response_dtos.*;

import com.example.library.services.BorrowRecordService;
import com.example.library.services.GoogleBooksService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import static java.util.Collections.sort;


@RestController
@RequestMapping("/api/borrowrecord")
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;
    private final GoogleBooksService googleBooksService;
    public BorrowRecordController(BorrowRecordService borrowRecordService, GoogleBooksService googleBooksService) {
       this.borrowRecordService = borrowRecordService;
        this.googleBooksService = googleBooksService;
    }
    @GetMapping
    public List<ResponseBorrowRecordDto> getAllBorrowRecords() {
        return borrowRecordService.getAllBorrowRecords();
    }
    @GetMapping("overdue")
    public List<ResponseBorrowRecordDto> getAllOverdueBorrowRecords() {
        return borrowRecordService.getAllOverdueBorrowRecords();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBorrowRecordDto> getBorrowRecordById(@PathVariable Long id) throws JSONException {
        var borrowRecord= borrowRecordService.getBorrowRecordById(id);
        if(borrowRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),borrowRecord.getBook().getStock(),new  ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getRatingsCount()),
                borrowRecord.getBorrowDate(),borrowRecord.getReturnDate()));
    }
    @GetMapping("search_studentbook/{id}")
    public List<ResponseBookDto> getBorrowRecordByStudent(@PathVariable Long id) {
        return borrowRecordService.getBorrowRecordByStudent(id);
    }
    @GetMapping("search_bookstudent/{id}")
    public List<ResponseStudentDto> getBorrowRecordByBook(@PathVariable Long id) {
        return borrowRecordService.getBorrowRecordByBook(id);
    }
    @GetMapping("mostborrowed/{num}")
    public List<ResponseBookNumDto> getMostBorrowedBooks(@PathVariable int num) {
        return borrowRecordService.getMostBorrowedBooks(num);
    }
    @PostMapping
    public ResponseEntity<ResponseBorrowRecordDto> createBorrowRecord(@RequestBody RequestBorrowRecordDto borrowRecord) throws JSONException {
        var borrowRecord2=borrowRecordService.createBorrowRecord(borrowRecord);
        if(borrowRecord2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord2.getStudent().getName(),borrowRecord2.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord2.getBook().getTitle(),borrowRecord2.getBook().getIsbn(),borrowRecord2.getBook().getStock(),new  ResponseAuthorDto(borrowRecord2.getBook().getAuthor().getName(),borrowRecord2.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getRatingsCount()),
                borrowRecord2.getBorrowDate(),borrowRecord2.getReturnDate()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBorrowRecordDto> updateBorrowRecord(@PathVariable Long id, @RequestBody RequestBorrowRecordDto borrowRecord) throws JSONException {
        var borrow=borrowRecordService.updateBorrowRecord(id, borrowRecord);
        if(borrow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrow.getStudent().getName(),borrow.getStudent().getEmail()),
                new ResponseBookDto(borrow.getBook().getTitle(),borrow.getBook().getIsbn(),borrow.getBook().getStock(),new  ResponseAuthorDto(borrow.getBook().getAuthor().getName(),borrow.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrow.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(borrow.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrow.getBook().getIsbn()).getAverageRating(),
                        googleBooksService.fetchBookByIsbn(borrow.getBook().getIsbn()).getRatingsCount()),
                borrow.getBorrowDate(),borrow.getReturnDate()));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        borrowRecordService.deleteStudentById(id);
    }

}
