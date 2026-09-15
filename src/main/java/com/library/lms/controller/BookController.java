package com.library.lms.controller;

import com.library.lms.dto.request.BookRequest;
import com.library.lms.dto.response.ApiResponse;
import com.library.lms.model.Book;
import com.library.lms.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> create(@Valid @RequestBody BookRequest request) {
        Book book = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm sách thành công", book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> update(@PathVariable Long id,
                                                      @Valid @RequestBody BookRequest request) {
        Book book = bookService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sách thành công", book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa sách thành công", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sách thành công", book));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Book>>> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<Book> books;
        if (title != null && !title.isBlank()) {
            books = bookService.searchByTitle(title, pageable);
        } else if (categoryId != null) {
            books = bookService.getByCategory(categoryId, pageable);
        } else if (authorId != null) {
            books = bookService.getByAuthor(authorId, pageable);
        } else {
            books = bookService.getAll(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sách thành công", books));
    }
}
