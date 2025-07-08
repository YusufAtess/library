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
    public BookCategory getBookCategoryById(@PathVariable Long id) {
        return bookCategoryRepository.findById(id).orElse(null);
    }
    @PostMapping
    public BookCategory createBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryRepository.save(bookCategory);
    }

    @PutMapping("{id}")
    public BookCategory updateBookCategory(@PathVariable Long id, @RequestBody BookCategory bookCategory) {
        return bookCategoryRepository.findById(id).map(bookCategory1 ->  {
            bookCategory1.setCategory( bookCategory.getCategory());
            bookCategory1.setBook( bookCategory.getBook());
            return bookCategoryRepository.save(bookCategory1);
        }).orElse(null);
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        bookCategoryRepository.deleteById(id);
    }

}
