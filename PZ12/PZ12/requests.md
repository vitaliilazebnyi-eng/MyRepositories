# Приклади запитів — Bookstore (заняття 1.2 — повний CRUD)

Бек має бути запущений: `./mvnw spring-boot:run` (порт 8080).

---

## 1. Перелік усіх книг (картки)

```bash
curl http://localhost:8080/api/books
```

Очікувана відповідь (JSON масив):
```json
[
  { "id": 1, "title": "Кобзар", "author": "Тарас Шевченко", "price": 250.00, "coverUrl": "/covers/kobzar.svg" },
  { "id": 2, "title": "Тіні забутих предків", "author": "Михайло Коцюбинський", "price": 180.50, "coverUrl": "/covers/tini.svg" }
]
```

---

## 2. Фільтрація книг за жанром

```bash
curl "http://localhost:8080/api/books?genre=Поезія"
```

Очікувано: книги тільки з жанром "Поезія".

---

## 3. Отримання однієї книги за id

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
  "genre": "Поезія",
  "coverUrl": "/covers/kobzar.svg",
  "description": "Збірка поетичних творів, що стала символом української літератури."
}
```

Неіснуюча книга (404):
```bash
curl -i http://localhost:8080/api/books/999
```
Очікувано: `HTTP/1.1 404`.

---

## 4. Створення нової книги (201 Created)

```bash
curl -i -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Нова книга","author":"Автор","price":199.00,"year":2024,"genre":"Проза","coverUrl":"/covers/x.jpg","description":"Опис нової книги"}'
```

Очікувано: `HTTP/1.1 201 Created` і JSON створеної книги з присвоєним `id`.

---

## 5. Оновлення існуючої книги (200 OK / 404 Not Found)

```bash
curl -i -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Кобзар (Ювілейне видання)","author":"Тарас Шевченко","price":350.00,"year":1840,"genre":"Поезія","coverUrl":"/covers/kobzar.svg","description":"Оновлений опис видання"}'
```

Очікувано: `HTTP/1.1 200 OK` і оновлений JSON.

Оновлення неіснуючої книги:
```bash
curl -i -X PUT http://localhost:8080/api/books/999 \
  -H "Content-Type: application/json" \
  -d '{"title":"Нічого","author":"Хтось","price":100.00,"year":2024,"genre":"Проза","coverUrl":"","description":""}'
```
Очікувано: `HTTP/1.1 404 Not Found`.

---

## 6. Видалення книги (204 No Content / 404 Not Found)

```bash
curl -i -X DELETE http://localhost:8080/api/books/2
```

Очікувано: `HTTP/1.1 204 No Content`.

Видалення вже видаленої або неіснуючої книги:
```bash
curl -i -X DELETE http://localhost:8080/api/books/999
```

Очікувано: `HTTP/1.1 404 Not Found`.

---

## 7. Роздача статичних файлів обкладинок

```bash
curl -i http://localhost:8080/covers/kobzar.svg
```

Очікувано: `HTTP/1.1 200 OK` і `Content-Type: image/svg+xml`.
