package com.ua.bookstore.config;

import com.ua.bookstore.domain.Book;
import com.ua.bookstore.repository.BookRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Початкове наповнення сховища під час запуску.
 * Поки немає POST-ендпоінта, нові книги додаються саме тут.
 */
@Component
public class BookSeeder implements ApplicationRunner {

    private final BookRepository repository;

    public BookSeeder(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        repository.save(new Book(null, "Кобзар", "Тарас Шевченко",
                new BigDecimal("250.00"), 1840, "/covers/kobzar.svg",
                "Збірка поетичних творів, що стала символом української літератури.",
                "Поезія"));

        repository.save(new Book(null, "Тіні забутих предків", "Михайло Коцюбинський",
                new BigDecimal("180.50"), 1911, "/covers/tini.svg",
                "Повість про кохання Івана й Марічки на тлі гуцульських вірувань і звичаїв Карпат.",
                "Повість"));

        repository.save(new Book(null, "Лісова пісня", "Леся Українка",
                new BigDecimal("210.00"), 1911, "/covers/lisova-pisnia.svg",
                "Драма-феєрія про кохання лісової мавки та селянського хлопця Лукаша.",
                "Драма"));

        repository.save(new Book(null, "Захар Беркут", "Іван Франко",
                new BigDecimal("195.00"), 1883, "/covers/zakhar-berkut.svg",
                "Історична повість про боротьбу карпатської громади проти монгольської навали.",
                "Історична проза"));

        repository.save(new Book(null, "Маруся Чурай", "Ліна Костенко",
                new BigDecimal("320.00"), 1979, "/covers/marusia-churai.svg",
                "Історичний роман у віршах про легендарну піснярку з Полтави XVII століття.",
                "Роман у віршах"));
    }
}
