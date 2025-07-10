package com.example.library.controller;


import com.example.library.request_dtos.RequestBorrowRecordDto;
import com.example.library.response_dtos.*;

import com.example.library.services.BorrowRecordService;
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

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
       this.borrowRecordService = borrowRecordService;
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
    public ResponseEntity<ResponseBorrowRecordDto> getBorrowRecordById(@PathVariable Long id) {
        var borrowRecord= borrowRecordService.getBorrowRecordById(id);
        if(borrowRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),borrowRecord.getBook().getStock(),new  ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality())),
                borrowRecord.getBorrowDate(),borrowRecord.getReturnDate()));
    }
    @GetMapping("search_studentbook/{id}")
    public List<ResponseBookDto> getBorrowRecordByStudent(@PathVariable Long id) {
        return borrowRecordService.getBorrowRecordByStudent(id);
    }
    @GetMapping("search_bookstudent/{id}")
    public List<ResponseStudentDto> getBorrowRecordByBook(@RequestParam Long id) {
        return borrowRecordService.getBorrowRecordByBook(id);
    }
    @GetMapping("mostborrowed/{num}")
    public List<ResponseBookNumDto> getMostBorrowedBooks(@PathVariable int num) {
        return borrowRecordService.getMostBorrowedBooks(num);
    }
    @PostMapping
    public ResponseEntity<ResponseBorrowRecordDto> createBorrowRecord(@RequestBody RequestBorrowRecordDto borrowRecord) {
        var borrowRecord2=borrowRecordService.createBorrowRecord(borrowRecord);
        if(borrowRecord2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord2.getStudent().getName(),borrowRecord2.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord2.getBook().getTitle(),borrowRecord2.getBook().getIsbn(),borrowRecord2.getBook().getStock(),new  ResponseAuthorDto(borrowRecord2.getBook().getAuthor().getName(),borrowRecord2.getBook().getAuthor().getNationality())),
                borrowRecord2.getBorrowDate(),borrowRecord2.getReturnDate()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBorrowRecordDto> updateBorrowRecord(@PathVariable Long id, @RequestBody RequestBorrowRecordDto borrowRecord) {
        var borrow=borrowRecordService.updateBorrowRecord(id, borrowRecord);
        if(borrow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBorrowRecordDto(new ResponseStudentDto(borrow.getStudent().getName(),borrow.getStudent().getEmail()),
                new ResponseBookDto(borrow.getBook().getTitle(),borrow.getBook().getIsbn(),borrow.getBook().getStock(),new  ResponseAuthorDto(borrow.getBook().getAuthor().getName(),borrow.getBook().getAuthor().getNationality())),
                borrow.getBorrowDate(),borrow.getReturnDate()));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        borrowRecordService.deleteStudentById(id);
    }

}
