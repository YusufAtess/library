package com.example.library.controller;


import com.example.library.response_dtos.ResponseAuthorDto;


import com.example.library.services.BookService;
import java.util.List;

import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.request_dtos.RequestBookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
      this.bookService = bookService;
    }
    @GetMapping
    public List<ResponseBookDto> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBookDto> getBookById(@PathVariable Long id) {
        var book= bookService.getBookById(id);
        if(book==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book.getTitle(),book.getIsbn(),
                new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality())));
    }
    @PostMapping
    public ResponseBookDto createBook(@RequestBody RequestBookDto book) {
      return bookService.createBook(book);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBookDto> updateBook(@PathVariable Long id, @RequestBody RequestBookDto book) {
        var book2= bookService.updateBook(id, book);
        if(book2==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book2.getTitle(),book2.getIsbn(),new ResponseAuthorDto(book2.getAuthor().getName(),book.getAuthor().getNationality())));
    }
    @DeleteMapping("{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
