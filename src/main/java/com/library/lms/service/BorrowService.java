package com.library.lms.service;

import com.library.lms.model.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowService {
    BorrowRecord borrowBook(String username, Long bookId);
    BorrowRecord returnBook(String username, Long borrowRecordId);
    Page<BorrowRecord> getMyBorrowRecords(String username, Pageable pageable);
    Page<BorrowRecord> getAllBorrowRecords(Pageable pageable);
}
