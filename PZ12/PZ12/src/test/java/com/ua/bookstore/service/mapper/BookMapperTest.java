package com.ua.bookstore.service.mapper;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/** UNIT-тест мапера domain → DTO та DTO → domain. */
class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    private Book sample() {
        return new Book(1L, "Кобзар", "Тарас Шевченко", new BigDecimal("250.00"),
                1840, "/covers/kobzar.svg", "Опис", "Поезія");
    }

    @Test
    void toCard_takesOnlyCardFields() {
        BookCardDto card = mapper.toCard(sample());

        assertThat(card.id()).isEqualTo(1L);
        assertThat(card.title()).isEqualTo("Кобзар");
        assertThat(card.author()).isEqualTo("Тарас Шевченко");
        assertThat(card.price()).isEqualByComparingTo("250.00");
        assertThat(card.coverUrl()).isEqualTo("/covers/kobzar.svg");
    }

    @Test
    void toDetail_copiesAllFields() {
        BookDetailDto detail = mapper.toDetail(sample());

        assertThat(detail.id()).isEqualTo(1L);
        assertThat(detail.year()).isEqualTo(1840);
        assertThat(detail.description()).isEqualTo("Опис");
        assertThat(detail.genre()).isEqualTo("Поезія");
    }

    @Test
    void toDomain_mapsRequestToBookWithoutId() {
        BookRequest request = new BookRequest("Нова книга", "Автор",
                new BigDecimal("199.00"), 2024, "Проза", "/covers/x.jpg", "Опис");

        Book book = mapper.toDomain(request);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo("Нова книга");
        assertThat(book.getAuthor()).isEqualTo("Автор");
        assertThat(book.getPrice()).isEqualByComparingTo("199.00");
        assertThat(book.getYear()).isEqualTo(2024);
        assertThat(book.getGenre()).isEqualTo("Проза");
        assertThat(book.getCoverUrl()).isEqualTo("/covers/x.jpg");
        assertThat(book.getDescription()).isEqualTo("Опис");
    }
}
