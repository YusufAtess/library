package com.example.library.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.services.AuthorService;
import java.util.stream.Collectors;
import com.example.library.request_dtos.RequestAuthorDto;
import com.example.library.response_dtos.ResponseAuthorDto;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorService authorService;
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    @GetMapping
    public List<ResponseAuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseAuthorDto> getAuthorById(@PathVariable Long id) {
        var author= authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseAuthorDto(author.getName(), author.getNationality()));
    }
    @PostMapping
    public ResponseAuthorDto createAuthor(@RequestBody RequestAuthorDto request_author) {
        return authorService.createAuthor(request_author);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseAuthorDto> updateAuthor(@PathVariable Long id, @RequestBody RequestAuthorDto author) {
        var author2= authorService.updateAuthor(id, author);
        if (author2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseAuthorDto(author.getName(), author.getNationality()));
    }
    @DeleteMapping("{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }

}

