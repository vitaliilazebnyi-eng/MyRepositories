package com.ua.bookstore.repository;

import com.ua.bookstore.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public List<Book> findAll() {
        // ConcurrentHashMap не гарантує порядок — сортуємо за id для стабільної відповіді
        List<Book> books = new ArrayList<>(storage.values());
        books.sort(Comparator.comparing(Book::getId));
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(sequence.incrementAndGet());
        }
        storage.put(book.getId(), book);
        return book;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        if (genre == null) {
            return List.of();
        }
        List<Book> books = new ArrayList<>(storage.values().stream()
                .filter(b -> b.getGenre() != null && genre.equalsIgnoreCase(b.getGenre()))
                .toList());
        books.sort(Comparator.comparing(Book::getId));
        return books;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return storage.remove(id) != null;
    }
}
