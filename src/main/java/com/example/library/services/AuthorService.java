package com.example.library.services;

import com.example.library.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Author;
import com.example.library.repository.AuthorRepository;
import java.util.List;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.request_dtos.RequestAuthorDto;
import java.util.stream.Collectors;


@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;


    public AuthorService(AuthorRepository authorRepository, BookService bookService, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    public List<ResponseAuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(author -> new ResponseAuthorDto(author.getName(), author.getNationality())).collect(Collectors.toList());
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public ResponseAuthorDto createAuthor(RequestAuthorDto request_author) {
        Author author = new Author();
        author.setName(request_author.getName());
        author.setNationality(request_author.getNationality());
        var savedauthor= authorRepository.save(author);
        return new ResponseAuthorDto(savedauthor.getName(), savedauthor.getNationality());
    }


    public Author updateAuthor(Long id,RequestAuthorDto author) {
        return  authorRepository.findById(id).map(author1 -> {
            author1.setName(author.getName());
            author1.setNationality(author.getNationality());
            return authorRepository.save(author1);
        }).orElse(null);
    }

    public void deleteAuthorById(Long id) {
        bookRepository.findByAuthor_Id(id).forEach(book -> bookRepository.deleteBookById(book.getId()));
        authorRepository.deleteById(id);
    }

}

