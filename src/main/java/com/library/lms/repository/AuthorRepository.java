package com.library.lms.repository;

import com.library.lms.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameIgnoreCase(String name);
}
