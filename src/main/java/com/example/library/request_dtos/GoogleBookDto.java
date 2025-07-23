package com.example.library.request_dtos;

import lombok.Data;

@Data
public class GoogleBookDto {
    private String thumbnail;
    private String publisher;
    private Integer averageRating;
    private Integer ratingsCount;
}
