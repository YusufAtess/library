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
import org.springframework.stereotype.Service;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCategoryRepository bookCategoryRepository;
    public BookService(BookRepository bookRepository,AuthorRepository authorRepository,
                       BorrowRecordRepository borrowRecordRepository,BookCategoryRepository bookCategoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    public List<ResponseBookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(book->new ResponseBookDto(book.getTitle(),book.getIsbn(),
                new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()))).collect(Collectors.toList());
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);

    }

    public ResponseBookDto createBook(RequestBookDto book) {
        Book book1= new Book();
        book1.setTitle(book.getTitle());
        book1.setIsbn(book.getIsbn());
        Author author=authorRepository.findById(book.getAuthor_id()).orElse(null);
        book1.setAuthor(author);
        Book book2= bookRepository.save(book1);
        return new ResponseBookDto(book2.getTitle(),book2.getIsbn(),new ResponseAuthorDto(author.getName(),author.getNationality()));
    }


    public Book updateBook(Long id, RequestBookDto book) {
        Author author=authorRepository.findById(book.getAuthor_id()).orElse(null);
        return  bookRepository.findById(id).map(book1 -> {
            book1.setTitle(book.getTitle());
            book1.setIsbn(book.getIsbn());
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

