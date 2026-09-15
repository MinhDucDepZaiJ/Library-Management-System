package com.library.lms.controller;

import com.library.lms.dto.request.AuthorRequest;
import com.library.lms.dto.response.ApiResponse;
import com.library.lms.model.Author;
import com.library.lms.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<ApiResponse<Author>> create(@Valid @RequestBody AuthorRequest request) {
        Author author = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm tác giả thành công", author));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Author>> update(@PathVariable Long id,
                                                        @Valid @RequestBody AuthorRequest request) {
        Author author = authorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật tác giả thành công", author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa tác giả thành công", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Author>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin tác giả thành công", authorService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Author>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tác giả thành công", authorService.getAll()));
    }
}
