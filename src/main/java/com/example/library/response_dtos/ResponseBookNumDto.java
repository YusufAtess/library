package com.example.library.response_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseBookNumDto {
    private String title;
    private String isbn;
    private int stock;
    private ResponseAuthorDto author;
    private long num;
}
