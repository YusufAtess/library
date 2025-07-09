package com.example.library.request_dtos;

import com.example.library.response_dtos.ResponseAuthorDto;
import lombok.Data;

@Data
public class RequestBookDto {
    private String title;
    private String isbn;
    private RequestAuthorDto author;
}
