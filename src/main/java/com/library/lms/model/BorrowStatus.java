package com.library.lms.model;

/**
 * Trạng thái của một phiếu mượn sách.
 */
public enum BorrowStatus {
    BORROWED,  // Đang mượn
    RETURNED,  // Đã trả
    OVERDUE    // Quá hạn
}
