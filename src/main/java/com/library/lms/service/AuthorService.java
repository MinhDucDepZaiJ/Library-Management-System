package com.library.lms.service;

import com.library.lms.dto.request.AuthorRequest;
import com.library.lms.model.Author;

import java.util.List;

public interface AuthorService {
    Author create(AuthorRequest request);
    Author update(Long id, AuthorRequest request);
    void delete(Long id);
    Author getById(Long id);
    List<Author> getAll();
}
