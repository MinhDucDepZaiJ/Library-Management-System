package com.library.lms.service.impl;

import com.library.lms.exception.BadRequestException;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.model.Book;
import com.library.lms.model.BorrowRecord;
import com.library.lms.model.BorrowStatus;
import com.library.lms.model.User;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowRecordRepository;
import com.library.lms.repository.UserRepository;
import com.library.lms.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private static final int BORROW_PERIOD_DAYS = 14;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BorrowRecord borrowBook(String username, Long bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với id: " + bookId));

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Sách '" + book.getTitle() + "' hiện không còn bản nào để mượn");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(BORROW_PERIOD_DAYS))
                .status(BorrowStatus.BORROWED)
                .build();

        return borrowRecordRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecord returnBook(String username, Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy phiếu mượn với id: " + borrowRecordId));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));

        boolean isOwner = record.getUser().getId().equals(currentUser.getId());
        boolean isStaff = currentUser.getRole().name().equals("ADMIN")
                || currentUser.getRole().name().equals("LIBRARIAN");

        if (!isOwner && !isStaff) {
            throw new AccessDeniedException("Bạn không có quyền trả sách của người khác");
        }

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new BadRequestException("Phiếu mượn này đã được trả trước đó");
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    @Override
    public Page<BorrowRecord> getMyBorrowRecords(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
        return borrowRecordRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    public Page<BorrowRecord> getAllBorrowRecords(Pageable pageable) {
        return borrowRecordRepository.findAll(pageable);
    }
}
