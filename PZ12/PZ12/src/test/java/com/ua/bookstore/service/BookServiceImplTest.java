package com.ua.bookstore.service;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.exception.BookNotFoundException;
import com.ua.bookstore.repository.BookRepository;
import com.ua.bookstore.service.mapper.BookMapper;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    private BookRequest sampleRequest() {
        return new BookRequest("Нова назва", "Новий автор", new BigDecimal("300.00"),
                2024, "Проза", "/covers/new.svg", "Новий опис");
    }

    @Test
    void findAll_withoutGenre_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(sampleBook()));

        List<BookCardDto> cards = service.findAll(null);

        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).id()).isEqualTo(1L);
        assertThat(cards.get(0).title()).isEqualTo("Кобзар");
        assertThat(cards.get(0).coverUrl()).isEqualTo("/covers/kobzar.svg");
    }

    @Test
    void findAll_withGenre_filtersByGenre() {
        when(repository.findByGenre("Поезія")).thenReturn(List.of(sampleBook()));

        List<BookCardDto> cards = service.findAll("Поезія");

        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).title()).isEqualTo("Кобзар");
        verify(repository).findByGenre("Поезія");
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

    @Test
    void create_savesAndReturnsDetail() {
        BookRequest request = sampleRequest();
        Book savedBook = new Book(10L, request.title(), request.author(), request.price(),
                request.year(), request.coverUrl(), request.description(), request.genre());

        when(repository.save(any(Book.class))).thenReturn(savedBook);

        BookDetailDto result = service.create(request);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.title()).isEqualTo("Нова назва");
        verify(repository).save(any(Book.class));
    }

    @Test
    void update_updatesExistingBook() {
        BookRequest request = sampleRequest();
        when(repository.findById(1L)).thenReturn(Optional.of(sampleBook()));

        Book updatedBook = new Book(1L, request.title(), request.author(), request.price(),
                request.year(), request.coverUrl(), request.description(), request.genre());
        when(repository.save(any(Book.class))).thenReturn(updatedBook);

        BookDetailDto result = service.update(1L, request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Нова назва");
        verify(repository).save(any(Book.class));
    }

    @Test
    void update_throwsWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, sampleRequest()))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void delete_removesBook() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(repository.deleteById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(BookNotFoundException.class);
    }
}
