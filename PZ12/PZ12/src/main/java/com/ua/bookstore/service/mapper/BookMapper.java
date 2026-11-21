package com.ua.bookstore.service.mapper;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookCardDto toCard(Book b) {
        return new BookCardDto(b.getId(), b.getTitle(), b.getAuthor(),
                b.getPrice(), b.getCoverUrl());
    }

    public BookDetailDto toDetail(Book b) {
        return new BookDetailDto(b.getId(), b.getTitle(), b.getAuthor(), b.getPrice(),
                b.getYear(), b.getCoverUrl(), b.getDescription(), b.getGenre());
    }

    public Book toDomain(BookRequest r) {
        Book b = new Book();
        b.setTitle(r.title());
        b.setAuthor(r.author());
        b.setPrice(r.price());
        b.setYear(r.year());
        b.setGenre(r.genre());
        b.setCoverUrl(r.coverUrl());
        b.setDescription(r.description());
        return b;
    }
}
