package com.library.lms.model;

/**
 * Vai trò người dùng trong hệ thống - dùng để phân quyền (Role-based Authorization).
 */
public enum Role {
    ADMIN,       // Toàn quyền quản trị hệ thống
    LIBRARIAN,   // Thủ thư: quản lý sách, tác giả, thể loại, mượn/trả
    USER         // Độc giả: xem sách, mượn/trả sách của chính mình
}
