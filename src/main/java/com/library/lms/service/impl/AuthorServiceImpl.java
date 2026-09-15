package com.library.lms.service.impl;

import com.library.lms.dto.request.AuthorRequest;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.model.Author;
import com.library.lms.repository.AuthorRepository;
import com.library.lms.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Author create(AuthorRequest request) {
        Author author = Author.builder()
                .name(request.getName())
                .biography(request.getBiography())
                .build();
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author update(Long id, AuthorRequest request) {
        Author author = getById(id);
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = getById(id);
        authorRepository.delete(author);
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tác giả với id: " + id));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
}
