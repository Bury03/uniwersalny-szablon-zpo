# Ściąga tematów przykładowych

Uwaga: te tematy są tylko przykładami. Nie zakładaj, że pojawią się na egzaminie. Używaj ich jako wzoru myślenia, jak dopasować nowy temat do szablonu.

Zalecany główny plik ze ściągą:

```text
docs/02_SCIAGA_MAPOWANIA_I_ALGORYTMOW.md
```

## Przykładowe mapowania

| Przykładowy temat | MainObject | AdditionalEntity | Reservation / operacja | Algorytm |
|---|---|---|---|---|
| Wypożyczalnia samochodów | samochód | typ auta | rezerwacja auta | cena i zniżka |
| System paczkomatów | skrytka | paczkomat/rozmiar | paczka | najmniejsza pasująca skrytka |
| Kino | seans | film/sala | rezerwacja miejsc | miejsca obok siebie |
| Weterynarz | lekarz | specjalizacja | wizyta | kolizja terminów |
| Parking | miejsce parkingowe | strefa | rezerwacja postoju | cena albo kolizja czasu |
| Biblioteka | książka | kategoria | wypożyczenie | termin zwrotu albo limit |
| Serwis komputerowy | technik/usługa | typ naprawy | zgłoszenie | status albo wycena |
| Siłownia | zajęcia/sala | typ zajęć | zapis | limit miejsc |

## Jak czytać tę tabelę

Nie kopiuj tematu dosłownie. Patrz, jaki jest typ problemu:

```text
czas -> kolizja terminów
cena -> liczenie kosztu
pojemność -> dobór najmniejszego pasującego zasobu
miejsca -> szukanie miejsc obok siebie
status -> kontrola przejść statusów
```
