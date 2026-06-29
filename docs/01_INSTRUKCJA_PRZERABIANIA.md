# Instrukcja przerabiania szablonu pod nowy temat

Ta instrukcja jest po to, żeby nie zaczynać projektu od zera.

## Krok 1. Ustal mapowanie tematu

Najpierw odpowiedz sobie na trzy pytania:

| Pytanie | Odpowiedź |
|---|---|
| Co jest głównym obiektem systemu? | `MainObject` |
| Jaka jest kategoria / typ tego obiektu? | `AdditionalEntity` |
| Co wykonuje użytkownik? | `Reservation` |

Przykład:

| Temat | MainObject | AdditionalEntity | Reservation |
|---|---|---|---|
| Wypożyczalnia aut | samochód | typ auta | rezerwacja auta |
| Weterynarz | lekarz | specjalizacja | wizyta |
| Paczkomat | skrytka | paczkomat albo rozmiar | paczka |
| Kino | seans | film | rezerwacja miejsc |

## Krok 2. Zmień dane startowe

Plik:

```text
src/main/java/com/example/egzamin/config/DataInitializer.java
```

Tu zmieniasz przykładowe kategorie i obiekty.

Nie ruszaj logiki tworzenia użytkowników, jeśli nie musisz.

## Krok 3. Zmień napisy w HTML-ach

Najczęściej zmieniasz pliki:

```text
src/main/resources/templates/index.html
src/main/resources/templates/user/reservation-form.html
src/main/resources/templates/user/my-reservations.html
src/main/resources/templates/admin/panel.html
src/main/resources/templates/admin/main-objects.html
src/main/resources/templates/admin/all-reservations.html
```

Zamieniasz słowa typu:

| Było | Zmieniasz na przykład na |
|---|---|
| Obiekt | Samochód / Lekarz / Skrytka / Seans |
| Rezerwacja | Wypożyczenie / Wizyta / Paczka / Bilet |
| Lokalizacja | Miejsce odbioru / Gabinet / Adres paczkomatu / Sala |
| Pojemność | Liczba miejsc / Rozmiar / Liczba osób |
| Cena | Cena za dzień / Cena wizyty / Cena biletu |

## Krok 4. Wybierz algorytm

Plik:

```text
src/main/java/com/example/egzamin/service/AlgorithmService.java
```

Gotowe algorytmy:

| Temat | Metoda |
|---|---|
| auta / hotel / sprzęt | `calculatePrice(...)` |
| weterynarz / terminy | `hasTimeConflict(...)` |
| paczkomaty | `findSmallestLockerCapacity(...)` |
| kino | `suggestSeatsTogether(...)` |

Nie musisz używać wszystkich algorytmów naraz.

## Krok 5. Dopasuj formularz

Plik:

```text
src/main/resources/templates/user/reservation-form.html
```

Masz tam pola:

| Pole | Do czego służy |
|---|---|
| `mainObjectId` | wybór głównego obiektu |
| `startDateTime` | początek |
| `endDateTime` | koniec |
| `note` | notatka |
| `extraData` | dane zależne od tematu |
| `discount` | przykładowa zniżka |

Jeśli któreś pole nie pasuje do tematu, możesz je ukryć albo zostawić jako dodatkową informację.

## Krok 6. Zostaw jeden test algorytmu

Plik:

```text
src/test/java/com/example/egzamin/service/AlgorithmServiceTest.java
```

Na zaliczenie wystarczy jeden dobry test, ale w szablonie jest kilka przykładów.

Dla konkretnego tematu możesz zostawić tylko ten, który pasuje.

## Krok 7. Sprawdź security

Wymaganie najczęściej brzmi:

- zwykły użytkownik widzi tylko swoje rezerwacje,
- admin widzi wszystko.

To jest już zrobione w:

```text
SecurityConfig.java
UserPanelController.java
AdminPanelController.java
ReservationService.java
```

## Krok 8. Sprawdź REST API

Gotowy endpoint:

```text
POST /api/operations
```

Możesz zmienić ścieżkę na bardziej tematyczną:

| Temat | Lepsza ścieżka |
|---|---|
| auta | `/api/reservations` |
| paczkomaty | `/api/parcels` |
| kino | `/api/tickets` |
| weterynarz | `/api/visits` |

Ale nie musisz. Dla punktów ważne jest, żeby endpoint działał.
