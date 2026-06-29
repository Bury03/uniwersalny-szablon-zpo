# Checklista punktów

## Baza danych: 1-2 pkt

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

- lista dostępnych obiektów,
- formularz operacji,
- zapis do bazy,
- wynik algorytmu zapisany w rezerwacji,
- widok moich rezerwacji,
- panel admina.

Najważniejszy plik:

```text
AlgorithmService.java
```

## Security: 4-5 pkt

Do pokazania:

- logowanie,
- wylogowanie,
- rola `USER`,
- rola `ADMIN`,
- user widzi tylko swoje rezerwacje,
- admin widzi wszystkie rezerwacje.

Najważniejsze pliki:

```text
SecurityConfig.java
CustomUserDetailsService.java
UserService.java
ReservationService.java
```

## REST API: 1-2 pkt

Do pokazania:

- `GET /api/main-objects`,
- `GET /api/main-objects/{id}/available`,
- `POST /api/operations`,
- Swagger UI.

Najważniejsze pliki:

```text
MainRestController.java
OperationRestController.java
OperationRequest.java
OperationResponse.java
```

## Testy: 1-2 pkt

Do pokazania:

- test algorytmu w `AlgorithmServiceTest.java`,
- uruchomienie testów,
- wynik zielony.

Najlepszy test na zaliczenie:

| Temat | Test |
|---|---|
| auta | cena ze zniżką |
| paczkomaty | wybór najmniejszej skrytki |
| kino | miejsca obok siebie |
| weterynarz | kolizja terminów |

## Minimalna ścieżka prezentacji

1. Otwórz stronę główną.
2. Zaloguj się jako `user`.
3. Dodaj rezerwację / operację.
4. Pokaż, że user widzi swoje dane.
5. Zaloguj się jako `admin`.
6. Pokaż, że admin widzi wszystko.
7. Otwórz Swagger.
8. Pokaż endpoint REST.
9. Uruchom test algorytmu.
