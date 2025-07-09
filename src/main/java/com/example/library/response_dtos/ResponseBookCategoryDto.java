package com.example.library.response_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ResponseBookCategoryDto {
    private ResponseCategoryDto category;
    private ResponseBookDto book;
}
