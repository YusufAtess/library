package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Book;
import com.example.library.repository.BookRepository;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        var book= bookRepository.findById(id).orElse(null);
        if(book==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        var book2= bookRepository.findById(id).map(book1 -> {
            book1.setTitle(book.getTitle());
            book1.setIsbn(book.getIsbn());
            book1.setAuthor(book.getAuthor());
            return bookRepository.save(book1);
        }).orElse(null);
        if(book2==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book2);
    }
    @DeleteMapping("{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

}
