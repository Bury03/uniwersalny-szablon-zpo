# Próba na losowym temacie

Ten plik pokazuje, jak sprawdzić, czy szablon jest naprawdę uniwersalny.

Przykładowy temat testowy:

```text
System rezerwacji miejsc parkingowych
```

To nie jest temat do nauczenia się na pamięć. To ćwiczenie pokazujące sposób myślenia.

## Krok 1. Mapowanie

| Element szablonu | Znaczenie w temacie parkingu |
|---|---|
| `MainObject` | miejsce parkingowe |
| `AdditionalEntity` | strefa parkingowa |
| `Reservation` | rezerwacja postoju |
| `location` | adres albo sektor parkingu |
| `capacity` | typ miejsca, np. 1 zwykłe, 2 większe |
| `price` | cena za godzinę |
| `extraData` | numer rejestracyjny auta |

## Krok 2. Algorytm

Najprostszy algorytm:

```text
sprawdź kolizję czasu dla danego miejsca parkingowego
```

Drugi możliwy algorytm:

```text
policz cenę postoju na podstawie czasu i ceny za godzinę
```

Na zaliczenie wybierz jeden główny algorytm i pokaż go dobrze.

## Krok 3. Dane startowe

Przykładowe `AdditionalEntity`:

```text
Strefa A
Strefa B
Strefa VIP
```

Przykładowe `MainObject`:

```text
Miejsce A1
Miejsce A2
Miejsce B1
Miejsce VIP1
```

## Krok 4. UI

Napisy do zmiany:

| Było | Ma być |
|---|---|
| Obiekt | Miejsce parkingowe |
| Rezerwacja | Rezerwacja postoju |
| Lokalizacja | Strefa / sektor |
| Pojemność | Typ miejsca |
| Cena | Cena za godzinę |
| Dane dodatkowe | Numer rejestracyjny |

## Krok 5. REST

Można zostawić:

```text
POST /api/operations
```

albo zmienić na:

```text
POST /api/parking-reservations
```

## Krok 6. Test

Przykładowy test:

```text
istnieje rezerwacja miejsca A1 od 10:00 do 12:00
nowa rezerwacja A1 od 11:00 do 13:00 powinna być odrzucona
```

albo:

```text
postój 3 godziny przy cenie 5 zł/h powinien kosztować 15 zł
```

## Wniosek

Jeżeli da się szybko zrobić parking, to szablon nie jest przywiązany do jednego tematu. Nadaje się do podobnych aplikacji, gdzie jest zasób, użytkownik, operacja, algorytm, baza danych, Security, REST i test.
