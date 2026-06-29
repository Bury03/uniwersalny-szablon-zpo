# Start tutaj

Ten plik otwierasz jako pierwszy, gdy dostajesz nowy temat aplikacji.

Celem nie jest zrobienie idealnej architektury domenowej. Celem jest szybkie zrobienie działającej aplikacji, która pokazuje bazę danych, UI, algorytm na backendzie, Security, REST API i test.

## Najważniejsze założenie

Przykłady z dokumentacji nie są listą tematów egzaminacyjnych. To tylko przykłady pokazujące, jak myśleć o przerabianiu szablonu.

Nowy temat najpierw sprowadź do trzech pytań:

| Pytanie | Odpowiedź w szablonie |
|---|---|
| Jaki jest główny zasób systemu? | `MainObject` |
| Jak pogrupować ten zasób? | `AdditionalEntity` |
| Co wykonuje użytkownik? | `Reservation` albo operacja |

## Szybka procedura

1. Wymyśl mapowanie tematu na `MainObject`, `AdditionalEntity`, `Reservation`.
2. Zapisz to w `messages.properties` jako notatkę dla siebie.
3. Zmień dane startowe w `DataInitializer.java`.
4. Zmień teksty w widokach HTML.
5. Wybierz albo dopisz algorytm w `AlgorithmService.java`.
6. Podepnij algorytm w `ReservationService.java` albo `OperationRestController.java`.
7. Dopasuj jeden endpoint REST.
8. Zostaw jeden test w `AlgorithmServiceTest.java`.
9. Sprawdź logowanie user/admin.
10. Zrób commit.

## Minimalny cel na zaliczenie

Aplikacja powinna pokazywać ten przepływ:

```text
user loguje się -> wybiera zasób -> wypełnia formularz -> backend sprawdza algorytm -> dane zapisują się w bazie -> user widzi swoje dane -> admin widzi wszystko
```

Jeżeli ten przepływ działa, masz pokrytą większość sensownych wymagań.
