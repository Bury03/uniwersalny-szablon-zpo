# Instrukcja przerabiania szablonu pod nowy temat

Ta instrukcja zakłada, że temat na egzaminie może być inny niż przykłady z zajęć. Dlatego nie próbuj najpierw zgadywać klas domenowych. Najpierw dopasuj temat do ogólnego szkieletu.

## Krok 1. Ustal mapowanie tematu

Wypełnij taką tabelę:

| Element | Co oznacza w nowym temacie |
|---|---|
| `MainObject` |  |
| `AdditionalEntity` |  |
| `Reservation` |  |
| `location` |  |
| `capacity` |  |
| `price` |  |
| `extraData` |  |

Przykłady mapowania:

| Typ tematu | MainObject | AdditionalEntity | Reservation / operacja |
|---|---|---|---|
| rezerwacje czasowe | zasób do rezerwacji | typ zasobu | rezerwacja terminu |
| wypożyczenia | rzecz do wypożyczenia | kategoria rzeczy | wypożyczenie |
| zgłoszenia | osoba albo usługa | specjalizacja/status | zgłoszenie |
| bilety/miejsca | wydarzenie albo seans | kategoria/film/sala | rezerwacja miejsc |
| magazyn/paczki | skrytka/miejsce | typ/rozmiar/lokalizacja | nadanie/przydział |

## Krok 2. Zmień dane startowe

Plik:

```text
src/main/java/com/example/egzamin/config/DataInitializer.java
```

Tu zmieniasz przykładowe kategorie i obiekty.

Zwykle zostawiasz tworzenie użytkowników:

```text
admin / admin123
user / user123
```

Zmieniasz głównie dane typu:

```text
AdditionalEntity -> kategorie, typy, specjalizacje, strefy
MainObject -> zasoby widoczne w aplikacji
```

## Krok 3. Zmień napisy w widokach

Najczęściej zmieniasz pliki:

```text
src/main/resources/templates/index.html
src/main/resources/templates/user/reservation-form.html
src/main/resources/templates/user/my-reservations.html
src/main/resources/templates/admin/panel.html
src/main/resources/templates/admin/main-objects.html
src/main/resources/templates/admin/all-reservations.html
```

Zamieniasz słowa ogólne na słowa z tematu:

| Ogólne słowo | Przykładowe znaczenie |
|---|---|
| Obiekt | zasób, auto, pokój, sprzęt, lekarz, miejsce |
| Rezerwacja | operacja, wypożyczenie, wizyta, zgłoszenie, zamówienie |
| Lokalizacja | adres, gabinet, sala, piętro, strefa, punkt odbioru |
| Pojemność | liczba miejsc, rozmiar, limit, liczba osób |
| Cena | cena za dzień, cena usługi, opłata, cena biletu |

## Krok 4. Wybierz albo dopisz algorytm

Plik:

```text
src/main/java/com/example/egzamin/service/AlgorithmService.java
```

W szablonie są przykładowe typy algorytmów:

| Typ wymagania | Metoda / pomysł |
|---|---|
| liczenie ceny | `calculatePrice(...)` |
| kolizja terminów | `hasTimeConflict(...)` |
| najmniejszy pasujący zasób | `findSmallestLockerCapacity(...)` |
| miejsca obok siebie | `suggestSeatsTogether(...)` |

Nie traktuj nazw dosłownie. Jeśli temat nie dotyczy skrytek, metoda z wyborem najmniejszego zasobu może nadal pasować, np. do wyboru najmniejszej sali, pojemnika, miejsca albo pakietu.

## Krok 5. Dopasuj formularz

Plik:

```text
src/main/resources/templates/user/reservation-form.html
```

Masz tam pola:

| Pole | Do czego służy |
|---|---|
| `mainObjectId` | wybór głównego zasobu |
| `startDateTime` | początek operacji, jeżeli temat ma czas |
| `endDateTime` | koniec operacji, jeżeli temat ma czas |
| `note` | zwykła notatka |
| `extraData` | dane zależne od tematu |
| `discount` | przykładowy checkbox do algorytmu |

Jeżeli temat nie ma dat, możesz zostawić daty jako uproszczenie albo ukryć je w widoku i ustawiać technicznie w serwisie.

## Krok 6. Dopasuj REST API

Gotowy endpoint:

```text
POST /api/operations
```

Dla punktów ważne jest, żeby endpoint działał. Możesz zostawić ogólną ścieżkę albo zmienić ją na tematyczną.

Przykłady:

| Typ tematu | Możliwa ścieżka |
|---|---|
| rezerwacje | `/api/reservations` |
| zgłoszenia | `/api/requests` |
| wypożyczenia | `/api/rentals` |
| bilety | `/api/tickets` |
| paczki | `/api/parcels` |

## Krok 7. Zostaw jeden test algorytmu

Plik:

```text
src/test/java/com/example/egzamin/service/AlgorithmServiceTest.java
```

Na zaliczenie wystarczy jeden dobry test, który pokazuje najważniejszą logikę.

Przykłady testów:

| Typ algorytmu | Co testować |
|---|---|
| cena | czy cena końcowa uwzględnia liczbę dni i zniżkę |
| kolizja | czy system wykrywa nakładające się terminy |
| dobór zasobu | czy system wybiera najmniejszy pasujący zasób |
| miejsca | czy system znajduje miejsca obok siebie |

## Krok 8. Sprawdź Security

Najczęstsze wymaganie:

```text
USER widzi tylko swoje rekordy
ADMIN widzi wszystkie rekordy
```

To jest obsługiwane w:

```text
SecurityConfig.java
UserPanelController.java
AdminPanelController.java
ReservationService.java
```

Jeżeli temat wymaga dodatkowej roli, np. kasjera albo kuriera, najprościej dodać ją do `Role.java` i przypisać odpowiednią ścieżkę w `SecurityConfig.java`.

## Krok 9. Uruchom i pokaż przepływ

Sprawdź:

```text
mvnw.cmd test
mvnw.cmd spring-boot:run
```

Potem pokaż:

1. stronę główną,
2. logowanie usera,
3. formularz operacji,
4. zapis danych,
5. widok danych usera,
6. panel admina,
7. Swagger,
8. test algorytmu.
