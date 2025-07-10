package com.example.library.request_dtos;

import com.example.library.response_dtos.ResponseBookDto;
import com.example.library.response_dtos.ResponseStudentDto;
import lombok.Data;
import lombok.Getter;
import org.apache.coyote.Request;

import java.util.Date;
@Data
public class RequestBorrowRecordDto {
    private long student_id;
    private long book_id;
    private Date borrowDate;
    private Date returnDate;
}
