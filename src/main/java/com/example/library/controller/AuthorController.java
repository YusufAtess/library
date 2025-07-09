package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Author;
import com.example.library.repository.AuthorRepository;
import java.util.List;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        var author= authorRepository.findById(id).orElse(null);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        var author2= authorRepository.findById(id).map(author1 -> {
            author1.setName(author.getName());
            author1.setNationality(author.getNationality());
            return authorRepository.save(author1);
        }).orElse(null);
        if (author2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author2);
    }
    @DeleteMapping("{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }

}

