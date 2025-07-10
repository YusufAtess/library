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
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.util.List;

@Service
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    public List<ResponseBookCategoryDto> getAllBookCategories() {
        return bookCategoryRepository.findAll().stream().map(bookCategory->
                        new ResponseBookCategoryDto(new ResponseCategoryDto(bookCategory.getCategory().getName()),
                                new ResponseBookDto(bookCategory.getBook().getTitle(),bookCategory.getBook().getIsbn(),new ResponseAuthorDto(bookCategory.getBook().getAuthor().getName(),bookCategory.getBook().getAuthor().getNationality()))))
                .collect(Collectors.toList());
    }

    public BookCategory getBookCategoryById(Long id) {
        return bookCategoryRepository.findById(id).orElse(null);
    }

    public ResponseBookCategoryDto createBookCategory(RequestBookCategoryDto bookCategory) {
        BookCategory bookCategory1 = new BookCategory();
        String isbn = bookCategory.getBook().getIsbn();
        String title = bookCategory.getBook().getTitle();
        String author_name = bookCategory.getBook().getAuthor().getName();
        String author_nationality = bookCategory.getBook().getAuthor().getNationality();
        String category_name = bookCategory.getCategory().getName();
        Author author = authorRepository.findByNameAndNationality(author_name, author_nationality).orElseGet(() -> authorRepository.save(new Author(null, author_name, author_nationality)));
        Book book = bookRepository.findByIsbn(isbn).orElseGet(() -> bookRepository.save(new Book(null, title, isbn, author)));
        Category category = categoryRepository.findByName(category_name).orElseGet(() -> categoryRepository.save(new Category(null, category_name)));
        bookCategory1.setBook(book);
        bookCategory1.setCategory(category);
        bookCategoryRepository.save(bookCategory1);
        return new ResponseBookCategoryDto(new ResponseCategoryDto(category_name),new ResponseBookDto(title,isbn,new ResponseAuthorDto(author_name,author_nationality)));
    }

    public BookCategory updateBookCategory(Long id,RequestBookCategoryDto bookCategory) {
        BookCategory bookCategory3 = new BookCategory();
        String isbn = bookCategory.getBook().getIsbn();
        String title = bookCategory.getBook().getTitle();
        String author_name = bookCategory.getBook().getAuthor().getName();
        String author_nationality = bookCategory.getBook().getAuthor().getNationality();
        String category_name = bookCategory.getCategory().getName();
        Author author = authorRepository.findByNameAndNationality(author_name, author_nationality).orElseGet(() -> authorRepository.save(new Author(null, author_name, author_nationality)));
        Book book = bookRepository.findByIsbn(isbn).orElseGet(() -> bookRepository.save(new Book(null, title, isbn, author)));
        Category category = categoryRepository.findByName(category_name).orElseGet(() -> categoryRepository.save(new Category(null, category_name)));
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

