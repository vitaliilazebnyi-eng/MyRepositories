package com.ua.bookstore.service;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.exception.BookNotFoundException;
import com.ua.bookstore.repository.BookRepository;
import com.ua.bookstore.service.mapper.BookMapper;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookServiceImpl(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BookCardDto> findAll(String genre) {
        List<Book> books = (genre == null || genre.isBlank())
                ? repository.findAll()
                : repository.findByGenre(genre);
        return books.stream().map(mapper::toCard).toList();
    }

    @Override
    public BookDetailDto findById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return mapper.toDetail(book);
    }

    @Override
    public BookDetailDto create(BookRequest request) {
        Book book = mapper.toDomain(request);
        book.setId(null);
        return mapper.toDetail(repository.save(book));
    }

    @Override
    public BookDetailDto update(Long id, BookRequest request) {
        repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        Book book = mapper.toDomain(request);
        book.setId(id);
        return mapper.toDetail(repository.save(book));
    }

    @Override
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new BookNotFoundException(id);
        }
    }
}
