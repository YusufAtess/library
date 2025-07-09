package com.example.library.response_dtos;

import lombok.*;

import java.util.Date;


@AllArgsConstructor
@Getter
public class ResponseBorrowRecordDto {
    private ResponseStudentDto student;
    private ResponseBookDto book;
    private Date borrowDate;
    private Date returnDate;
}
