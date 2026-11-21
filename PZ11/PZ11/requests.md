# Приклади запитів — Bookstore (заняття 1/1)

Бек має бути запущений: `./mvnw spring-boot:run` (порт 8080).

На цьому занятті лише **два GET-ендпоінти** (перегляд).

---

## 1. Перелік книг (картки)

```bash
curl http://localhost:8080/api/books
```

Очікувана відповідь (скорочено):
```json
[
  { "id": 1, "title": "Кобзар", "author": "Тарас Шевченко", "price": 250.00, "coverUrl": "/covers/kobzar.svg" },
  { "id": 2, "title": "Тіні забутих предків", "author": "Михайло Коцюбинський", "price": 180.50, "coverUrl": "/covers/tini.svg" }
]
```

Красивий вивід (якщо встановлено `jq`):
```bash
curl -s http://localhost:8080/api/books | jq
```

---

## 2. Одна книга з деталями

```bash
curl http://localhost:8080/api/books/1
```

Очікувана відповідь:
```json
{
  "id": 1,
  "title": "Кобзар",
  "author": "Тарас Шевченко",
  "price": 250.00,
  "year": 1840,
  "coverUrl": "/covers/kobzar.svg",
  "description": "Збірка поетичних творів, що стала символом української літератури.",
  "genre": "Поезія"
}
```

---

## 3. Книга, якої немає → 404

```bash
curl -i http://localhost:8080/api/books/999
```

Очікувано: перший рядок `HTTP/1.1 404`.

---

## 4. Обкладинки віддаються як статика

```bash
curl -i http://localhost:8080/covers/kobzar.svg
```

Очікувано: `HTTP/1.1 200` і `Content-Type: image/svg+xml`.
