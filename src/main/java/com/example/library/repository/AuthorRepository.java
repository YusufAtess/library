package com.example.library.repository;

import com.example.library.entities.Author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameAndNationality(String name, String nationality);

}
