package com.example.library.repository;

import com.example.library.entities.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStudent_Id(Long id);
    @Query("SELECT b FROM BorrowRecord b WHERE b.returnDate IS NULL AND b.borrowDate < :threshold")
    List<BorrowRecord> findOverdueRecords(@Param("threshold") Date threshold);
    long countByBook_Id(Long bookId);
    List<BorrowRecord> findByBook_Id(Long id);


}



