package com.ua.bookstore.service.mapper;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/** UNIT-тест мапера domain → DTO. */
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
}
