package com.example.library.request_dtos;

import lombok.Data;

@Data
public class RequestBookCategoryDto {
    private RequestCategoryDto category;
    private RequestBookDto book;
}
