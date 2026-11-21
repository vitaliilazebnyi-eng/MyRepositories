package com.ua.bookstore.service;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.exception.BookNotFoundException;
import com.ua.bookstore.repository.BookRepository;
import com.ua.bookstore.service.mapper.BookMapper;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** UNIT-тест сервісу: репозиторій замокано, маппер справжній. */
class BookServiceImplTest {

    private final BookRepository repository = mock(BookRepository.class);
    private final BookMapper mapper = new BookMapper();
    private final BookService service = new BookServiceImpl(repository, mapper);

    private Book sampleBook() {
        return new Book(1L, "Кобзар", "Тарас Шевченко", new BigDecimal("250.00"),
                1840, "/covers/kobzar.svg", "Опис", "Поезія");
    }

    @Test
    void findAll_mapsBooksToCards() {
        when(repository.findAll()).thenReturn(List.of(sampleBook()));

        List<BookCardDto> cards = service.findAll();

        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).id()).isEqualTo(1L);
        assertThat(cards.get(0).title()).isEqualTo("Кобзар");
        assertThat(cards.get(0).coverUrl()).isEqualTo("/covers/kobzar.svg");
    }

    @Test
    void findById_returnsDetail() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleBook()));

        BookDetailDto detail = service.findById(1L);

        assertThat(detail.id()).isEqualTo(1L);
        assertThat(detail.description()).isEqualTo("Опис");
        assertThat(detail.genre()).isEqualTo("Поезія");
        assertThat(detail.year()).isEqualTo(1840);
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(BookNotFoundException.class);
    }
}
