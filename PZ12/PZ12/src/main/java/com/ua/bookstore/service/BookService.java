package com.ua.bookstore.service;

import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;

import java.util.List;

public interface BookService {
    List<BookCardDto> findAll(String genre);

    BookDetailDto findById(Long id);

    BookDetailDto create(BookRequest request);

    BookDetailDto update(Long id, BookRequest request);

    void delete(Long id);
}
