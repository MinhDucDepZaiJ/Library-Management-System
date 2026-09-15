package com.library.lms.service;

import com.library.lms.dto.request.BookRequest;
import com.library.lms.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Book create(BookRequest request);
    Book update(Long id, BookRequest request);
    void delete(Long id);
    Book getById(Long id);
    Page<Book> getAll(Pageable pageable);
    Page<Book> searchByTitle(String title, Pageable pageable);
    Page<Book> getByCategory(Long categoryId, Pageable pageable);
    Page<Book> getByAuthor(Long authorId, Pageable pageable);
}
