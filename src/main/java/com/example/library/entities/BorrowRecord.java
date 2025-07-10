package com.example.library.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
@Data

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Many borrow records can belong to one book
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate; // Nullable by default


}

