package com.library.lms.service.impl;

import com.library.lms.dto.request.CategoryRequest;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.model.Category;
import com.library.lms.repository.CategoryRepository;
import com.library.lms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại với id: " + id));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
