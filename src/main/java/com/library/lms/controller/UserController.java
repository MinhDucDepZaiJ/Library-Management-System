package com.library.lms.controller;

import com.library.lms.dto.request.UpdateRoleRequest;
import com.library.lms.dto.response.ApiResponse;
import com.library.lms.model.User;
import com.library.lms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Quản lý người dùng - chỉ ADMIN mới được truy cập (đã cấu hình trong SecurityConfig).
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAll() {
        return ApiResponse.success("Lấy danh sách người dùng thành công", userService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        return ApiResponse.success("Lấy thông tin người dùng thành công", userService.getById(id));
    }

    @PutMapping("/{id}/role")
    public ApiResponse<User> updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        User user = userService.updateRole(id, request.getRole());
        return ApiResponse.success("Cập nhật vai trò thành công", user);
    }

    @PutMapping("/{id}/enable")
    public ApiResponse<User> enable(@PathVariable Long id) {
        return ApiResponse.success("Kích hoạt tài khoản thành công", userService.setEnabled(id, true));
    }

    @PutMapping("/{id}/disable")
    public ApiResponse<User> disable(@PathVariable Long id) {
        return ApiResponse.success("Vô hiệu hóa tài khoản thành công", userService.setEnabled(id, false));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success("Xóa người dùng thành công", null);
    }
}
