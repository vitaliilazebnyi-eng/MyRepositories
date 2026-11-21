package com.ua.bookstore.web;

import com.ua.bookstore.exception.BookNotFoundException;
import com.ua.bookstore.service.BookService;
import com.ua.bookstore.web.dto.BookCardDto;
import com.ua.bookstore.web.dto.BookDetailDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        when(bookService.findAll()).thenReturn(List.of(
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
}
