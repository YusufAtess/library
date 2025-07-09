package com.example.library.response_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseBookDto {
    private String title;
    private String isbn;
    private ResponseAuthorDto author;
}
