package com.library.lms.repository;

import com.library.lms.model.BorrowRecord;
import com.library.lms.model.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    Page<BorrowRecord> findByUserId(Long userId, Pageable pageable);

    Page<BorrowRecord> findByStatus(BorrowStatus status, Pageable pageable);

    long countByBookIdAndStatus(Long bookId, BorrowStatus status);
}
