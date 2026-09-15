package com.library.lms.controller;

import com.library.lms.dto.request.CategoryRequest;
import com.library.lms.dto.response.ApiResponse;
import com.library.lms.model.Category;
import com.library.lms.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm thể loại thành công", category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> update(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thể loại thành công", category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thể loại thành công", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thể loại thành công", categoryService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thể loại thành công", categoryService.getAll()));
    }
}
