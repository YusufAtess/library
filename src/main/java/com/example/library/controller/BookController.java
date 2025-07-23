package com.example.library.controller;


import com.example.library.response_dtos.ResponseAuthorDto;


import com.example.library.services.BookService;
import java.util.List;

import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.request_dtos.RequestBookDto;
import com.example.library.services.GoogleBooksService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/book")
public class BookController {
    private final GoogleBooksService googleBooksService;
    private final BookService bookService;
    public BookController(GoogleBooksService googleBooksService, BookService bookService) {
        this.googleBooksService = googleBooksService;
        this.bookService = bookService;
    }
    @GetMapping
    public List<ResponseBookDto> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBookDto> getBookById(@PathVariable Long id) throws JSONException {
        var book= bookService.getBookById(id);
        if(book==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book.getTitle(),book.getIsbn(),
                book.getStock(),new  ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(book.getIsbn()).getThumbnail(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getPublisher(),
                googleBooksService.fetchBookByIsbn(book.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getRatingsCount()));
    }
    @PostMapping
    public ResponseEntity<ResponseBookDto> createBook(@RequestBody RequestBookDto book) throws JSONException {
        var book2= bookService.createBook(book);
        if(book2.getAuthor()==null)ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ResponseBookDto(book2.getTitle(),book2.getIsbn(), book2.getStock(),new  ResponseAuthorDto(book2.getAuthor().getName(),book2.getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(book2.getIsbn()).getThumbnail(),
                googleBooksService.fetchBookByIsbn(book2.getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book2.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book2.getIsbn()).getRatingsCount()));

    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBookDto> updateBook(@PathVariable Long id, @RequestBody RequestBookDto book) throws JSONException {
        var book2= bookService.updateBook(id, book);
        if(book2==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book2.getTitle(),book2.getIsbn(),book2.getStock(),new  ResponseAuthorDto(book2.getAuthor().getName(),book2.getAuthor().getNationality()),googleBooksService.fetchBookByIsbn(book2.getIsbn()).getThumbnail(),
                googleBooksService.fetchBookByIsbn(book2.getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book2.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book2.getIsbn()).getRatingsCount()));
    }
    @DeleteMapping("{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

}
