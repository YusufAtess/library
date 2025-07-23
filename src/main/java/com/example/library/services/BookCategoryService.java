package com.example.library.services;

import com.example.library.entities.Author;
import com.example.library.entities.Book;
import com.example.library.entities.Category;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.response_dtos.ResponseAuthorDto;
import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.response_dtos.ResponseCategoryDto;

import com.example.library.entities.BookCategory;
import com.example.library.repository.BookCategoryRepository;
import com.example.library.request_dtos.RequestBookCategoryDto;
import com.example.library.response_dtos.ResponseBookCategoryDto;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.util.List;

@Service
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final GoogleBooksService googleBooksService;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository, GoogleBooksService googleBooksService) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.googleBooksService = googleBooksService;
    }

    public List<ResponseBookCategoryDto> getAllBookCategories() {
        return bookCategoryRepository.findAll().stream().map(bookCategory->
                {
                    try {
                        return new ResponseBookCategoryDto(new ResponseCategoryDto(bookCategory.getCategory().getName()),
                                new ResponseBookDto(bookCategory.getBook().getTitle(),bookCategory.getBook().getIsbn(),bookCategory.getBook().getStock(),new  ResponseAuthorDto(bookCategory.getBook().getAuthor().getName(),bookCategory.getBook().getAuthor().getNationality()),googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getThumbnail(),
                                        googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getAverageRating(),
                                        googleBooksService.fetchBookByIsbn(bookCategory.getBook().getIsbn()).getRatingsCount()));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public BookCategory getBookCategoryById(Long id) {
        return bookCategoryRepository.findById(id).orElse(null);
    }

    public ResponseBookCategoryDto createBookCategory(RequestBookCategoryDto bookCategory) throws JSONException {
        BookCategory bookCategory1 = new BookCategory();

        Book book = bookRepository.findById(bookCategory.getBook_id()).orElseGet(null);
        Category category = categoryRepository.findById(bookCategory.getCategory_id()).orElseGet(null);
        bookCategory1.setBook(book);
        bookCategory1.setCategory(category);
        bookCategoryRepository.save(bookCategory1);
        return new ResponseBookCategoryDto(new ResponseCategoryDto(category.getName()),new ResponseBookDto(book.getTitle(),book.getIsbn(),book.getStock(),new  ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()),googleBooksService.fetchBookByIsbn(book.getIsbn()).getThumbnail(),
                googleBooksService.fetchBookByIsbn(book.getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getRatingsCount()));
    }

    public BookCategory updateBookCategory(Long id,RequestBookCategoryDto bookCategory) {
        BookCategory bookCategory3 = new BookCategory();
        Book book = bookRepository.findById(bookCategory.getBook_id()).orElse(null);
        Category category = categoryRepository.findById(bookCategory.getCategory_id()).orElse(null);
        bookCategory3.setBook(book);
        bookCategory3.setCategory(category);
        return bookCategoryRepository.findById(id).map(bookCategory1 ->  {
            bookCategory1.setCategory( bookCategory3.getCategory());
            bookCategory1.setBook( bookCategory3.getBook());
            return bookCategoryRepository.save(bookCategory1);
        }).orElse(null);

    }

    public void deleteStudentById(Long id) {
        bookCategoryRepository.deleteById(id);
    }

}

