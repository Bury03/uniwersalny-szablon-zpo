# Checklista punktów

Ta checklista pomaga sprawdzić, czy projekt pokazuje wszystkie obszary zwykle oceniane w prostych aplikacjach Spring Boot.

## Baza danych: zwykle 1-2 pkt

Do pokazania:

- encje w `entity`,
- repozytoria w `repository`,
- relacje `User -> Reservation`, `MainObject -> Reservation`, `AdditionalEntity -> MainObject`,
- dane startowe w `DataInitializer`,
- H2 Console.

Minimum:

```text
User
MainObject
AdditionalEntity
Reservation
```

## UI i algorytm: największa część punktów

Do pokazania:

- lista dostępnych zasobów,
- formularz operacji,
- zapis do bazy,
- algorytm wykonany na backendzie,
- wynik algorytmu zapisany albo pokazany użytkownikowi,
- widok danych użytkownika,
- panel admina.

Najważniejszy plik:

```text
AlgorithmService.java
```

Ważne: frontend ma głównie wyświetlać dane. Algorytm powinien być w serwisie, nie w HTML-u ani JavaScripcie.

## Security: zwykle 4-5 pkt

Do pokazania:

- logowanie,
- wylogowanie,
- rola `USER`,
- rola `ADMIN`,
- user widzi tylko swoje rekordy,
- admin widzi wszystkie rekordy.

Najważniejsze pliki:

```text
SecurityConfig.java
CustomUserDetailsService.java
UserService.java
ReservationService.java
```

## REST API: zwykle 1-2 pkt

Do pokazania:

- Swagger UI,
- przynajmniej jeden endpoint `GET` albo `POST`,
- najlepiej endpoint związany z główną operacją aplikacji.

Gotowe endpointy:

```text
GET /api/main-objects
GET /api/main-objects/{id}/available
POST /api/operations
```

Najważniejsze pliki:

```text
MainRestController.java
OperationRestController.java
OperationRequest.java
OperationResponse.java
```

## Testy: zwykle 1-2 pkt

Do pokazania:

- uruchomienie testów,
- przynajmniej jeden test algorytmu,
- wynik `BUILD SUCCESS`.

Komenda:

```text
mvnw.cmd test
```

Najważniejszy plik:

```text
AlgorithmServiceTest.java
```

## Szybka kolejność prezentacji

1. Pokaż stronę główną.
2. Zaloguj się jako user.
3. Wykonaj operację z formularza.
4. Pokaż, że rekord zapisał się na koncie usera.
5. Zaloguj się jako admin.
6. Pokaż, że admin widzi wszystkie rekordy.
7. Otwórz Swagger.
8. Pokaż endpoint REST.
9. Uruchom test algorytmu.

## Co mówić przy prezentacji

Możesz powiedzieć krótko:

```text
Najważniejsza logika jest na backendzie w AlgorithmService. Frontend służy głównie do wyświetlania danych i wysyłania formularza. Dostęp do danych jest ograniczony przez Spring Security. Zwykły użytkownik widzi tylko swoje rekordy, a administrator widzi wszystko.
```
