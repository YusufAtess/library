package com.example.library.services;

import com.example.library.entities.*;
import com.example.library.request_dtos.RequestBorrowRecordDto;
import com.example.library.response_dtos.*;


import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.BorrowRecordRepository;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.util.Collections.sort;


@Service
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final GoogleBooksService googleBooksService;

    public BorrowRecordService(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository, StudentRepository studentRepository, AuthorRepository authorRepository, GoogleBooksService googleBooksService) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.googleBooksService = googleBooksService;

    }

    public List<ResponseBorrowRecordDto> getAllBorrowRecords() {
        return borrowRecordRepository.findAll().stream().map(borrowRecord-> {
                    try {
                        return new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                                        new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),borrowRecord.getBook().getStock(),new  ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getThumbnail(),
                                                googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getAverageRating(),
                                                googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getRatingsCount()),
                                        borrowRecord.getBorrowDate(),borrowRecord.getReturnDate());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<ResponseBorrowRecordDto> getAllOverdueBorrowRecords() {
        LocalDate today = LocalDate.now();
        LocalDate fourteenDaysAgo = today.minusDays(14);
        Date thresholdDate = Date.from(fourteenDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return borrowRecordRepository.findOverdueRecords(thresholdDate).stream().map(borrowRecord-> {
                    try {
                        return new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()),
                                        new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),borrowRecord.getBook().getStock(),new  ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getThumbnail(),
                                                googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getRatingsCount()),
                                        borrowRecord.getBorrowDate(),borrowRecord.getReturnDate());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public BorrowRecord getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id).orElse(null);

    }

    public List<ResponseBookDto> getBorrowRecordByStudent(Long id) {
        return borrowRecordRepository.findByStudent_Id(id).stream().map(borrowRecord-> {
            try {
                return new ResponseBookDto(borrowRecord.getBook().getTitle(),borrowRecord.getBook().getIsbn(),
                        borrowRecord.getBook().getStock(),new  ResponseAuthorDto(borrowRecord.getBook().getAuthor().getName(),borrowRecord.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getAverageRating(),
                        googleBooksService.fetchBookByIsbn(borrowRecord.getBook().getIsbn()).getRatingsCount());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
    public List<ResponseStudentDto> getBorrowRecordByBook(Long id) {
        return borrowRecordRepository.findByBook_Id(id).stream().map(borrowRecord->new ResponseStudentDto(borrowRecord.getStudent().getName(),borrowRecord.getStudent().getEmail()
                )).collect(Collectors.toList());
    }

    public List<ResponseBookNumDto> getMostBorrowedBooks(int num) {
        List<ResponseBookNumDto> sortedBooks= bookRepository.findAll().stream().map(book-> {
            try {
                return new ResponseBookNumDto(book.getTitle(),book.getIsbn(),
                       book.getStock(), new ResponseAuthorDto(book.getAuthor().getName(),book.getAuthor().getNationality()),borrowRecordRepository.countByBook_Id(book.getId()), googleBooksService.fetchBookByIsbn(book.getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(book.getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(book.getIsbn()).getRatingsCount());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        sortedBooks.sort(Comparator.comparing(ResponseBookNumDto::getNum).reversed());
        int size=sortedBooks.size();
        size=(num>size)?size:num;
        return sortedBooks.subList(0,size);


    }

    public ResponseBorrowRecordDto createBorrowRecord(RequestBorrowRecordDto borrowRecord) throws JSONException {
        BorrowRecord borrowRecord1 = new BorrowRecord();
        Book book = bookRepository.findById(borrowRecord.getBook_id()).orElse(null);
        if(book.getStock()<1)return null;
        book.setStock(book.getStock()-1);
        Student student = studentRepository.findById(borrowRecord.getStudent_id()).orElse(null);
        borrowRecord1.setBook(book);
        borrowRecord1.setStudent(student);
        borrowRecord1.setBorrowDate(borrowRecord.getBorrowDate());
        borrowRecord1.setReturnDate(borrowRecord.getReturnDate());
        var borrowRecord2 = borrowRecordRepository.save(borrowRecord1);
        return new ResponseBorrowRecordDto(new ResponseStudentDto(borrowRecord2.getStudent().getName(),borrowRecord2.getStudent().getEmail()),
                new ResponseBookDto(borrowRecord2.getBook().getTitle(),borrowRecord2.getBook().getIsbn(),borrowRecord2.getBook().getStock(),new  ResponseAuthorDto(borrowRecord2.getBook().getAuthor().getName(),borrowRecord2.getBook().getAuthor().getNationality()), googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getThumbnail(),
                        googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getPublisher(),googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getAverageRating(),googleBooksService.fetchBookByIsbn(borrowRecord2.getBook().getIsbn()).getRatingsCount()),
                borrowRecord2.getBorrowDate(),borrowRecord2.getReturnDate());
    }


    public BorrowRecord updateBorrowRecord(Long id,RequestBorrowRecordDto borrowRecord) {
        BorrowRecord borrowRecord1 = new BorrowRecord();
        Book book = bookRepository.findById(borrowRecord.getBook_id()).orElse(null);
        if(book.getStock()<1)return null;
        book.setStock(book.getStock()-1);
        Student student = studentRepository.findById(borrowRecord.getStudent_id()).orElse(null);
        borrowRecord1.setStudent(student);
        borrowRecord1.setBook(book);
        return borrowRecordRepository.findById(id).map(borrowRecord2 -> {
            borrowRecord2.getBook().setStock(borrowRecord2.getBook().getStock()+1);
            borrowRecord2.setStudent(borrowRecord1.getStudent());
            borrowRecord2.setBook(borrowRecord1.getBook());
            borrowRecord2.setReturnDate(borrowRecord.getReturnDate());
            borrowRecord2.setBorrowDate(borrowRecord.getBorrowDate());
            return borrowRecordRepository.save(borrowRecord2);
        }).orElse(null);

    }

    public void deleteStudentById(Long id) {
        var borrowRecord=borrowRecordRepository.findById(id).orElse(null);
        borrowRecord.getBook().setStock(borrowRecord.getBook().getStock()+1);
        borrowRecordRepository.deleteById(id);
    }

}

