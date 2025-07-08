package com.example.library.repository;

import com.example.library.entities.BookCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}
