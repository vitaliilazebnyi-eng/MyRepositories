package com.ua.bookstore.web;

import com.ua.bookstore.exception.BookNotFoundException;
import com.ua.bookstore.service.BookService;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import com.ua.bookstore.web.dto.BookRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** INTEGRATION-тест веб-шару: лише контролер, сервіс замокано. */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void list_returnsBookCards() throws Exception {
        when(bookService.findAll(null)).thenReturn(List.of(
                new BookCardDto(1L, "Кобзар", "Тарас Шевченко",
                        new BigDecimal("250.00"), "/covers/kobzar.svg")));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Кобзар"))
                .andExpect(jsonPath("$[0].author").value("Тарас Шевченко"))
                .andExpect(jsonPath("$[0].coverUrl").value("/covers/kobzar.svg"));
    }

    @Test
    void list_withGenre_filtersBooks() throws Exception {
        when(bookService.findAll("Поезія")).thenReturn(List.of(
                new BookCardDto(1L, "Кобзар", "Тарас Шевченко",
                        new BigDecimal("250.00"), "/covers/kobzar.svg")));

        mockMvc.perform(get("/api/books").param("genre", "Поезія"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Кобзар"));
    }

    @Test
    void getOne_returnsBookDetail() throws Exception {
        when(bookService.findById(1L)).thenReturn(new BookDetailDto(
                1L, "Кобзар", "Тарас Шевченко", new BigDecimal("250.00"),
                1840, "/covers/kobzar.svg", "Опис книги", "Поезія"));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.year").value(1840))
                .andExpect(jsonPath("$.description").value("Опис книги"))
                .andExpect(jsonPath("$.genre").value("Поезія"));
    }

    @Test
    void getOne_returns404WhenMissing() throws Exception {
        when(bookService.findById(999L)).thenThrow(new BookNotFoundException(999L));

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returns201AndCreatedBook() throws Exception {
        BookDetailDto created = new BookDetailDto(10L, "Нова книга", "Автор",
                new BigDecimal("199.00"), 2024, "/covers/x.jpg", "Опис", "Проза");
        when(bookService.create(any(BookRequest.class))).thenReturn(created);

        String json = """
                {
                  "title": "Нова книга",
                  "author": "Автор",
                  "price": 199.00,
                  "year": 2024,
                  "genre": "Проза",
                  "coverUrl": "/covers/x.jpg",
                  "description": "Опис"
                }
                """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Нова книга"))
                .andExpect(jsonPath("$.price").value(199.00));
    }

    @Test
    void update_returns200AndUpdatedBook() throws Exception {
        BookDetailDto updated = new BookDetailDto(1L, "Оновлена книга", "Тарас Шевченко",
                new BigDecimal("300.00"), 1840, "/covers/kobzar.svg", "Оновлено", "Поезія");
        when(bookService.update(eq(1L), any(BookRequest.class))).thenReturn(updated);

        String json = """
                {
                  "title": "Оновлена книга",
                  "author": "Тарас Шевченко",
                  "price": 300.00,
                  "year": 1840,
                  "genre": "Поезія",
                  "coverUrl": "/covers/kobzar.svg",
                  "description": "Оновлено"
                }
                """;

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Оновлена книга"));
    }

    @Test
    void update_returns404WhenMissing() throws Exception {
        when(bookService.update(eq(999L), any(BookRequest.class)))
                .thenThrow(new BookNotFoundException(999L));

        String json = """
                {
                  "title": "Нічого",
                  "author": "Хтось",
                  "price": 100.00,
                  "year": 2024,
                  "genre": "Проза",
                  "coverUrl": "",
                  "description": ""
                }
                """;

        mockMvc.perform(put("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204NoContent() throws Exception {
        doNothing().when(bookService).delete(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_returns404WhenMissing() throws Exception {
        doThrow(new BookNotFoundException(999L)).when(bookService).delete(999L);

        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());
    }
}
