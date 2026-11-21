package com.ua.bookstore.web.dto;

import java.math.BigDecimal;

/** DTO для переліку: мінімум полів для картки книги. */
public record BookCardDto(Long id, String title, String author,
                          BigDecimal price, String coverUrl) {
}
