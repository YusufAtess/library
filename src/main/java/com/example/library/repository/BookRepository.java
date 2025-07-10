package com.example.library.repository;

import com.example.library.entities.Book;

import com.example.library.entities.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
Optional<Book> findByIsbn(String isbn);
Optional<Book> findByTitle(String title);
List<Book> findByAuthor_Id(Long id);

    void deleteBookById(Long id);
}
