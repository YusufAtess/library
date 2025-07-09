package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.BorrowRecord;
import com.example.library.repository.BorrowRecordRepository;
import java.util.List;

@RestController
@RequestMapping("/api/borrowrecord")
public class BorrowRecordController {
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowRecordController(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }
    @GetMapping
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<BorrowRecord> getBorrowRecordById(@PathVariable Long id) {
        var borrow= borrowRecordRepository.findById(id).orElse(null);
        if(borrow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(borrow);
    }
    @PostMapping
    public BorrowRecord createBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        return borrowRecordRepository.save(borrowRecord);
    }

    @PutMapping("{id}")
    public ResponseEntity<BorrowRecord> updateBorrowRecord(@PathVariable Long id, @RequestBody BorrowRecord borrowRecord) {
        var borrow= borrowRecordRepository.findById(id).map(borrowRecord1 -> {
            borrowRecord1.setStudent(borrowRecord.getStudent());
            borrowRecord1.setBook(borrowRecord.getBook());
            return borrowRecordRepository.save(borrowRecord1);
        }).orElse(null);
        if(borrow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(borrow);
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        borrowRecordRepository.deleteById(id);
    }

}
