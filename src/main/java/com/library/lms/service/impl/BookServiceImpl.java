package com.library.lms.service.impl;

import com.library.lms.dto.request.BookRequest;
import com.library.lms.exception.BadRequestException;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.model.Author;
import com.library.lms.model.Book;
import com.library.lms.model.Category;
import com.library.lms.repository.AuthorRepository;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.CategoryRepository;
import com.library.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Book create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("ISBN đã tồn tại: " + request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .publishedYear(request.getPublishedYear())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .build();

        applyAuthorAndCategory(book, request);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Long id, BookRequest request) {
        Book book = getById(id);

        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("ISBN đã tồn tại: " + request.getIsbn());
        }

        int borrowedCount = book.getQuantity() - book.getAvailableQuantity();
        if (request.getQuantity() < borrowedCount) {
            throw new BadRequestException(
                    "Số lượng mới không thể nhỏ hơn số sách đang được mượn (" + borrowedCount + ")");
        }

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setDescription(request.getDescription());
        book.setAvailableQuantity(request.getQuantity() - borrowedCount);
        book.setQuantity(request.getQuantity());

        applyAuthorAndCategory(book, request);

        return bookRepository.save(book);
    }

    private void applyAuthorAndCategory(Book book, BookRequest request) {
        if (request.getAuthorId() != null) {
            Author author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy tác giả với id: " + request.getAuthorId()));
            book.setAuthor(author);
        } else {
            book.setAuthor(null);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy thể loại với id: " + request.getCategoryId()));
            book.setCategory(category);
        } else {
            book.setCategory(null);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với id: " + id));
    }

    @Override
    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> searchByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Book> getByCategory(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Book> getByAuthor(Long authorId, Pageable pageable) {
        return bookRepository.findByAuthorId(authorId, pageable);
    }
}
