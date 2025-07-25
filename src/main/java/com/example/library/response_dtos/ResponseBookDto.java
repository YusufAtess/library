package com.example.library.response_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseBookDto {
    private String title;
    private String isbn;
    private int stock;
    private ResponseAuthorDto author;
    private String thumbnail;
    private String publisher;
    private Integer averageRating;
    private Integer ratingsCount;
}
