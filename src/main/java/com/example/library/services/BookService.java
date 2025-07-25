package com.example.library.services;

import com.example.library.entities.Author;
import com.example.library.repository.BookCategoryRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.response_dtos.ResponseAuthorDto;


import com.example.library.entities.Book;
import com.example.library.repository.BookRepository;
import com.example.library.repository.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.request_dtos.RequestBookDto;
import org.json.JSONException;
import org.springframework.stereotype.Service;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GoogleBooksService googleBooksService;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCategoryRepository bookCategoryRepository;
    public BookService(BookRepository bookRepository,AuthorRepository authorRepository,
                       BorrowRecordRepository borrowRecordRepository,BookCategoryRepository bookCategoryRepository,
                       GoogleBooksService googleBooksService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookCategoryRepository = bookCategoryRepository;
        this.googleBooksService = googleBooksService;
    }

    public List<ResponseBookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book-> {
            try {
                return new ResponseBookDto(book.getTitle(),book.getIsbn(),
                        book.getStock(), new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(book.getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(book.getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getRatingsCount());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);

    }

    public Book createBook(RequestBookDto book) {
        Book book1= new Book();
        book1.setTitle(book.getTitle());
        book1.setIsbn(book.getIsbn());
        book1.setStock(book.getStock());
        Author author=authorRepository.findById(book.getAuthor_id()).orElse(null);
        book1.setAuthor(author);
        return bookRepository.save(book1);

    }


    public Book updateBook(Long id, RequestBookDto book) {
        Author author=authorRepository.findById(book.getAuthor_id()).orElse(null);
        return  bookRepository.findById(id).map(book1 -> {
            book1.setTitle(book.getTitle());
            book1.setIsbn(book.getIsbn());
            book1.setStock(book.getStock());
            book1.setAuthor(author);
            return bookRepository.save(book1);
        }).orElse(null);
    }

    public void deleteBookById(Long id) {
        borrowRecordRepository.findByBook_Id(id).forEach(record -> borrowRecordRepository.deleteById(record.getId()));
        bookCategoryRepository.findByBook_Id(id).forEach(record -> bookCategoryRepository.deleteById(record.getId()));
        bookRepository.deleteById(id);
    }
}

