package com.example.library.repository;

import com.example.library.entities.BookCategory;

import com.example.library.entities.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    List<BookCategory> findByCategory_Id(Long categoryId);
    List<BookCategory> findByBook_Id(Long bookId);
}
