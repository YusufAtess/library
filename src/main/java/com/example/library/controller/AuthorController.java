package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Author;
import com.example.library.repository.AuthorRepository;
import java.util.List;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.request_dtos.RequestAuthorDto;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    @GetMapping
    public List<ResponseAuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(author -> new ResponseAuthorDto(author.getName(), author.getNationality())).collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseAuthorDto> getAuthorById(@PathVariable Long id) {
        var author= authorRepository.findById(id).orElse(null);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseAuthorDto(author.getName(), author.getNationality()));
    }
    @PostMapping
    public ResponseAuthorDto createAuthor(@RequestBody RequestAuthorDto request_author) {
        Author author = new Author();
        author.setName(request_author.getName());
        author.setNationality(request_author.getNationality());
        var savedauthor= authorRepository.save(author);
        return new ResponseAuthorDto(savedauthor.getName(), savedauthor.getNationality());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseAuthorDto> updateAuthor(@PathVariable Long id, @RequestBody RequestAuthorDto author) {
        var author2= authorRepository.findById(id).map(author1 -> {
            author1.setName(author.getName());
            author1.setNationality(author.getNationality());
            return authorRepository.save(author1);
        }).orElse(null);
        if (author2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseAuthorDto(author.getName(), author.getNationality()));
    }
    @DeleteMapping("{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }

}

