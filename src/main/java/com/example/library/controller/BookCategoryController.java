package com.example.library.controller;

import com.example.library.services.BookCategoryService;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.response_dtos.ResponseCategoryDto;
import com.example.library.services.BookCategoryService;
import com.example.library.services.BookService;
import com.example.library.services.GoogleBooksService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.library.request_dtos.RequestBookCategoryDto;
import com.example.library.response_dtos.ResponseBookCategoryDto;


import java.util.List;

@RestController
@RequestMapping("/api/bookcategory")
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;
    private final BookService bookService;
    private final GoogleBooksService googleBooksService;
    public BookCategoryController(BookCategoryService bookCategoryService, BookService bookService, GoogleBooksService googleBooksService) {
       this.bookCategoryService = bookCategoryService;
        this.bookService = bookService;
        this.googleBooksService = googleBooksService;
    }
    @GetMapping
    public List<ResponseBookCategoryDto> getAllBookCategories() {
        return bookCategoryService.getAllBookCategories();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBookCategoryDto> getBookCategoryById(@PathVariable Long id) throws JSONException {
        var bookCategory= bookCategoryService.getBookCategoryById(id);
        if(bookCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookCategoryDto(new ResponseCategoryDto(bookCategory.getCategory().getName()),
                new ResponseBookDto(bookCategory.getBook().getTitle(),bookCategory.getBook().getIsbn(),
                       bookCategory.getBook().getStock(), new ResponseAuthorDto(bookCategory.getBook().getAuthor().getName(),bookCategory.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getRatingsCount())));
    }
    @PostMapping
    public ResponseBookCategoryDto createBookCategory(@RequestBody RequestBookCategoryDto bookCategory) throws JSONException {
        return bookCategoryService.createBookCategory(bookCategory);
    }
    @PutMapping("{id}")
    public ResponseEntity<ResponseBookCategoryDto> updateBookCategory(@PathVariable Long id, @RequestBody RequestBookCategoryDto bookCategory) throws JSONException {
        var book2=bookCategoryService.updateBookCategory(id, bookCategory);
        if(book2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookCategoryDto(new ResponseCategoryDto(book2.getCategory().getName()),new ResponseBookDto(book2.getBook().getTitle(),book2.getBook().getIsbn(),book2.getBook().getStock(),new  ResponseAuthorDto(book2.getBook().getAuthor().getName(),book2.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(book2.getBook().getIsbn()).getThumbnail(),
                googleBooksService.fetchBookByIsbn(book2.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book2.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book2.getBook().getIsbn()).getRatingsCount())));
    }
    @DeleteMapping("{id}")
    public void deleteStudentById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
