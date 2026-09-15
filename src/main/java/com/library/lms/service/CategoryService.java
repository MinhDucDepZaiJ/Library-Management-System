package com.library.lms.service;

import com.library.lms.dto.request.CategoryRequest;
import com.library.lms.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(CategoryRequest request);
    Category update(Long id, CategoryRequest request);
    void delete(Long id);
    Category getById(Long id);
    List<Category> getAll();
}
