package com.library.lms.repository;

import com.library.lms.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
}
