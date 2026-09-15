package com.library.lms.controller;

import com.library.lms.dto.response.ApiResponse;
import com.library.lms.model.BorrowRecord;
import com.library.lms.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    // Mượn sách - bất kỳ người dùng đã đăng nhập nào
    @PostMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BorrowRecord>> borrowBook(@PathVariable Long bookId,
                                                                  Authentication authentication) {
        BorrowRecord record = borrowService.borrowBook(authentication.getName(), bookId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mượn sách thành công", record));
    }

    // Trả sách - chủ sở hữu phiếu mượn hoặc ADMIN/LIBRARIAN
    @PutMapping("/{recordId}/return")
    public ResponseEntity<ApiResponse<BorrowRecord>> returnBook(@PathVariable Long recordId,
                                                                  Authentication authentication) {
        BorrowRecord record = borrowService.returnBook(authentication.getName(), recordId);
        return ResponseEntity.ok(ApiResponse.success("Trả sách thành công", record));
    }

    // Xem lịch sử mượn sách của chính mình
    @GetMapping("/my-records")
    public ResponseEntity<ApiResponse<Page<BorrowRecord>>> getMyRecords(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<BorrowRecord> records = borrowService.getMyBorrowRecords(authentication.getName(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy lịch sử mượn sách thành công", records));
    }

    // Xem toàn bộ phiếu mượn trong hệ thống - chỉ ADMIN/LIBRARIAN
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<ApiResponse<Page<BorrowRecord>>> getAllRecords(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<BorrowRecord> records = borrowService.getAllBorrowRecords(pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy toàn bộ phiếu mượn thành công", records));
    }
}
