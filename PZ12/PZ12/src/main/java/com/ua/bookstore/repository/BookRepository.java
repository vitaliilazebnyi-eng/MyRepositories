package com.ua.bookstore.repository;

import com.ua.bookstore.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    List<Book> findByGenre(String genre);

    Book save(Book book);

    boolean deleteById(Long id);
}
