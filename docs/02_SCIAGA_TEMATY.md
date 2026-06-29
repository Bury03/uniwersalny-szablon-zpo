# Ściąga tematów

## 1. Wypożyczalnia samochodów

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | samochód |
| `AdditionalEntity` | typ auta |
| `Reservation` | rezerwacja auta |
| `location` | miejsce odbioru |
| `capacity` | liczba miejsc |
| `price` | cena za dzień |
| `extraData` | informacje o kierowcy |

Algorytm:

```text
liczba dni * cena za dzień - zniżka
```

Najprostsza zniżka:

```text
od 7 dni -> 10%
od 14 dni -> 15%
dodatkowy checkbox -> 5%
```

Co pokazać:

1. User wybiera auto i termin.
2. System blokuje zajęty termin.
3. System liczy cenę.
4. User widzi swoje rezerwacje.
5. Admin widzi wszystkie.
6. REST tworzy rezerwację.
7. Test sprawdza cenę.

## 2. System paczkomatów

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | skrytka |
| `AdditionalEntity` | paczkomat albo rozmiar |
| `Reservation` | paczka / nadanie paczki |
| `location` | adres paczkomatu |
| `capacity` | pojemność skrytki jako liczba |
| `price` | opcjonalna cena nadania |
| `extraData` | gabaryt paczki / dane odbiorcy |

Gabaryty:

```text
S = 1
M = 2
L = 3
```

Algorytm:

```text
wybierz najmniejszą wolną skrytkę, która mieści paczkę
```

Co pokazać:

1. User nadaje paczkę.
2. Podaje gabaryt.
3. System wybiera najmniejszą pasującą skrytkę.
4. User widzi swoje paczki.
5. Admin albo kurier widzi paczki do obsługi.
6. REST nadaje paczkę.
7. Test sprawdza wybór skrytki.

Uwaga:
Jeśli prowadzący wymaga osobnej roli `COURIER`, dodaj ją do `Role.java` i w `SecurityConfig.java` ustaw osobną ścieżkę, np. `/courier/**`.

## 3. Rezerwacja biletów w kinie

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | seans |
| `AdditionalEntity` | film |
| `Reservation` | rezerwacja miejsc |
| `location` | sala kinowa |
| `capacity` | liczba miejsc |
| `price` | cena biletu |
| `extraData` | miejsca, np. A1,A2,A3 |

Algorytm:

```text
znajdź pierwsze wolne miejsca obok siebie
```

Najprostsza sala:

```text
A1 A2 A3 A4 A5
B1 B2 B3 B4 B5
C1 C2 C3 C4 C5
```

Co pokazać:

1. User wybiera seans.
2. Wpisuje liczbę miejsc albo miejsca.
3. System proponuje miejsca obok siebie.
4. Rezerwacja zapisuje miejsca w `extraData`.
5. User widzi swoje bilety.
6. Kasjer/admin widzi wszystkie rezerwacje.
7. REST zwraca albo tworzy rezerwację miejsc.
8. Test sprawdza proponowanie miejsc.

## 4. Umawianie wizyt do weterynarza

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | lekarz weterynarii |
| `AdditionalEntity` | specjalizacja |
| `Reservation` | wizyta |
| `location` | gabinet |
| `capacity` | opcjonalnie liczba wizyt dziennie |
| `price` | cena wizyty |
| `extraData` | imię zwierzęcia / opis problemu |

Algorytm:

```text
nowa wizyta nie może nachodzić na inną wizytę tego samego lekarza
```

Co pokazać:

1. User wybiera lekarza.
2. User wybiera termin.
3. System blokuje kolizję czasową.
4. User widzi swoje wizyty.
5. Admin widzi wszystkie wizyty.
6. REST tworzy wizytę.
7. Test sprawdza kolizję terminów.
