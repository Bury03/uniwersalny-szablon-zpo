# Ściąga mapowania i algorytmów

Ten plik nie opisuje pewnych tematów egzaminacyjnych. To ściąga, która pomaga szybko rozpoznać, jaki typ problemu masz przed sobą.

## 1. Temat z rezerwacją w czasie

Przykłady podobnych tematów:

```text
hotel, gabinet, lekarz, weterynarz, sala, auto, sprzęt, boisko, miejsce parkingowe
```

Mapowanie:

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | zasób rezerwowany w czasie |
| `AdditionalEntity` | typ, kategoria, specjalizacja albo lokalizacja |
| `Reservation` | rezerwacja / wizyta / wynajem |
| `startDateTime`, `endDateTime` | termin |
| `price` | cena za jednostkę czasu |

Algorytm:

```text
sprawdź, czy nowy termin nie nachodzi na istniejący termin tego samego zasobu
```

Test:

```text
istnieje termin 10:00-11:00, nowy termin 10:30-11:30 powinien dać konflikt
```

## 2. Temat z liczeniem ceny

Przykłady podobnych tematów:

```text
wypożyczenie, hotel, parking, bilet, usługa, abonament, zamówienie
```

Mapowanie:

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | płatny zasób albo usługa |
| `price` | cena bazowa |
| `Reservation.totalPrice` | cena końcowa |
| `discount` | zniżka albo dodatkowy warunek |

Algorytm:

```text
cena bazowa * ilość - zniżka
```

Test:

```text
dla 10 dni i zniżki system powinien zwrócić niższą cenę niż bez zniżki
```

## 3. Temat z doborem najmniejszego pasującego zasobu

Przykłady podobnych tematów:

```text
paczkomat, magazyn, sala, pojemnik, miejsce parkingowe, transport, przydział sprzętu
```

Mapowanie:

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | dostępny zasób do przydzielenia |
| `capacity` | pojemność, rozmiar, limit |
| `extraData` | wymagany rozmiar albo potrzeba użytkownika |

Algorytm:

```text
znajdź wszystkie zasoby, które spełniają wymaganie, a potem wybierz najmniejszy pasujący
```

Test:

```text
dla wymaganego rozmiaru 2 i dostępnych zasobów 1, 2, 3 system powinien wybrać 2
```

## 4. Temat z miejscami obok siebie

Przykłady podobnych tematów:

```text
kino, teatr, sala konferencyjna, stadion, autobus, samolot
```

Mapowanie:

| Element szablonu | Znaczenie |
|---|---|
| `MainObject` | seans, wydarzenie albo przejazd |
| `AdditionalEntity` | film, sala, kategoria wydarzenia |
| `Reservation.extraData` | wybrane miejsca |

Algorytm:

```text
znajdź ciąg wolnych miejsc obok siebie
```

Test:

```text
jeżeli A1 i A2 są zajęte, a trzeba znaleźć 3 miejsca, system powinien zaproponować pierwszy wolny ciąg 3 miejsc
```

## 5. Temat ze statusem

Przykłady podobnych tematów:

```text
paczki, zgłoszenia serwisowe, zamówienia, zadania, naprawy, wypożyczenia
```

Mapowanie:

| Element szablonu | Znaczenie |
|---|---|
| `Reservation` | rekord operacji |
| `ReservationStatus` | aktualny status |
| `note` | opis zgłoszenia |
| `extraData` | dane specyficzne |

Algorytm:

```text
zmień status tylko wtedy, gdy przejście jest dozwolone
```

Przykład:

```text
NOWE -> W_TRAKCIE -> ZAKONCZONE
```

W obecnym szablonie status rezerwacji już istnieje. Przy takim temacie można go wykorzystać zamiast dodawać nowe klasy.

## Szybka decyzja

Gdy dostajesz temat, wybierz główny typ:

| Wymaganie w treści | Najbliższy typ algorytmu |
|---|---|
| terminy, wizyty, dostępność | kolizja czasowa |
| cena, zniżka, koszt | liczenie ceny |
| gabaryt, limit, pojemność | najmniejszy pasujący zasób |
| miejsca obok siebie | ciąg miejsc |
| statusy, obsługa, anulowanie | przejścia statusów |

Potem nie komplikuj. Lepiej mieć jeden prosty działający algorytm niż pięć zaczętych.
