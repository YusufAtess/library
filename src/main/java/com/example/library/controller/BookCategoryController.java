package com.example.library.controller;

import com.example.library.services.BookCategoryService;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.response_dtos.ResponseCategoryDto;
import com.example.library.services.BookCategoryService;
import com.example.library.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.library.request_dtos.RequestBookCategoryDto;
import com.example.library.response_dtos.ResponseBookCategoryDto;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/api/bookcategory")
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;
    private final BookService bookService;

    public BookCategoryController(BookCategoryService bookCategoryService, BookService bookService) {
       this.bookCategoryService = bookCategoryService;
        this.bookService = bookService;
    }
    @GetMapping
    public List<ResponseBookCategoryDto> getAllBookCategories() {
        return bookCategoryService.getAllBookCategories();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBookCategoryDto> getBookCategoryById(@PathVariable Long id) {
        var bookCategory= bookCategoryService.getBookCategoryById(id);
        if(bookCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookCategoryDto(new ResponseCategoryDto(bookCategory.getCategory().getName()),
                new ResponseBookDto(bookCategory.getBook().getTitle(),bookCategory.getBook().getIsbn(),
                       bookCategory.getBook().getStock(), new ResponseAuthorDto(bookCategory.getBook().getAuthor().getName(),bookCategory.getBook().getAuthor().getNationality()))));
    }
    @PostMapping
    public ResponseBookCategoryDto createBookCategory(@RequestBody RequestBookCategoryDto bookCategory) {
        return bookCategoryService.createBookCategory(bookCategory);
    }
    @PutMapping("{id}")
    public ResponseEntity<ResponseBookCategoryDto> updateBookCategory(@PathVariable Long id, @RequestBody RequestBookCategoryDto bookCategory) {
        var book2=bookCategoryService.updateBookCategory(id, bookCategory);
        if(book2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookCategoryDto(new ResponseCategoryDto(book2.getCategory().getName()),new ResponseBookDto(book2.getBook().getTitle(),book2.getBook().getIsbn(),book2.getBook().getStock(),new  ResponseAuthorDto(book2.getBook().getAuthor().getName(),book2.getBook().getAuthor().getNationality()))));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
