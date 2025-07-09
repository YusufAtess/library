package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.BookCategory;
import com.example.library.repository.BookCategoryRepository;
import java.util.List;

@RestController
@RequestMapping("/api/bookcategory")
public class BookCategoryController {
    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryController(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }
    @GetMapping
    public List<BookCategory> getAllBookCategories() {
        return bookCategoryRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<BookCategory> getBookCategoryById(@PathVariable Long id) {
        var book= bookCategoryRepository.findById(id).orElse(null);
        if(book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @PostMapping
    public BookCategory createBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryRepository.save(bookCategory);
    }

    @PutMapping("{id}")
    public ResponseEntity<BookCategory> updateBookCategory(@PathVariable Long id, @RequestBody BookCategory bookCategory) {
        var book= bookCategoryRepository.findById(id).map(bookCategory1 ->  {
            bookCategory1.setCategory( bookCategory.getCategory());
            bookCategory1.setBook( bookCategory.getBook());
            return bookCategoryRepository.save(bookCategory1);
        }).orElse(null);
        if(book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        bookCategoryRepository.deleteById(id);
    }

}
