package com.example.library.entities;

import jakarta.persistence.*;
import lombok.*;
@Data

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nationality;




}


