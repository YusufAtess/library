package com.example.library.controller;

import com.example.library.entities.Author;
import com.example.library.response_dtos.ResponseAuthorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Book;
import com.example.library.repository.BookRepository;
import com.example.library.repository.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.request_dtos.RequestBookDto;


@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    public BookController(BookRepository bookRepository,AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }
    @GetMapping
    public List<ResponseBookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book->new ResponseBookDto(book.getTitle(),book.getIsbn(),
                new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()))).collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseBookDto> getBookById(@PathVariable Long id) {
        var book= bookRepository.findById(id).orElse(null);
        if(book==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book.getTitle(),book.getIsbn(),
                new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality())));
    }
    @PostMapping
    public ResponseBookDto createBook(@RequestBody RequestBookDto book) {
        Book book1= new Book();
        book1.setTitle(book.getTitle());
        book1.setIsbn(book.getIsbn());
        String name=book.getAuthor().getName();
        String nationality=book.getAuthor().getNationality();
        Author author=authorRepository.findByNameAndNationality(name,nationality).orElseGet(() -> authorRepository.save(new Author(null,name,nationality)));
        book1.setAuthor(author);
        Book book2= bookRepository.save(book1);
        return new ResponseBookDto(book2.getTitle(),book2.getIsbn(),new ResponseAuthorDto(book2.getAuthor().getName(),book.getAuthor().getNationality()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBookDto> updateBook(@PathVariable Long id, @RequestBody RequestBookDto book) {
        String nationality=book.getAuthor().getNationality();
        String name=book.getAuthor().getName();
        Author author=authorRepository.findByNameAndNationality(name,nationality).orElseGet(() -> authorRepository.save(new Author(null,name,nationality)));
        var book2= bookRepository.findById(id).map(book1 -> {
            book1.setTitle(book.getTitle());
            book1.setIsbn(book.getIsbn());
            book1.setAuthor(author);
            return bookRepository.save(book1);
        }).orElse(null);
        if(book2==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBookDto(book2.getTitle(),book2.getIsbn(),new ResponseAuthorDto(book2.getAuthor().getName(),book.getAuthor().getNationality())));
    }
    @DeleteMapping("{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

}
