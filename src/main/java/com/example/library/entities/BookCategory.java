package com.example.library.entities;

import jakarta.persistence.*;
import lombok.*;
@Data

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many borrow records can belong to one student
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many borrow records can belong to one book
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


}
