package com.example.egzamin.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
    ============================================================
    PLIK TECHNICZNY STAŁY: DateTimeUtils.java
    ============================================================

    To jest klasa pomocnicza do pracy z datą i godziną.

    Nie jest encją.
    Nie jest kontrolerem.
    Nie jest serwisem.

    To zwykła klasa narzędziowa.

    Przydaje się np. do:

    - formatowania daty do ładnego tekstu,
    - przygotowania daty do inputa HTML datetime-local,
    - sprawdzania, czy data jest w przyszłości,
    - sprawdzania, czy koniec jest po początku.

    Ten plik jest uniwersalny i pasuje do każdego tematu:

    - rezerwacje sal,
    - auta,
    - hotel,
    - sprzęt,
    - gabinety,
    - usługi.
*/
public final class DateTimeUtils {

    /*
        ============================================================
        FORMAT DO WYŚWIETLANIA DATY UŻYTKOWNIKOWI
        ============================================================

        Ten format daje np.:

        28.06.2026 10:30

        Czyli format bardziej czytelny dla człowieka.
    */
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /*
        ============================================================
        FORMAT DO HTML DATETIME-LOCAL
        ============================================================

        Input w HTML-u:

        <input type="datetime-local">

        potrzebuje formatu:

        2026-06-28T10:30

        Tam pomiędzy datą a godziną jest litera T.
    */
    private static final DateTimeFormatter HTML_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /*
        ============================================================
        PRYWATNY KONSTRUKTOR
        ============================================================

        Ta klasa ma tylko metody statyczne.

        Nie chcemy robić:

        new DateTimeUtils();

        Dlatego konstruktor jest private.
    */
    private DateTimeUtils() {
    }

    /*
        ============================================================
        FORMATOWANIE DATY DO WYŚWIETLENIA
        ============================================================

        Zamienia LocalDateTime na tekst czytelny dla użytkownika.

        Przykład:

        LocalDateTime:
        2026-06-28T10:30

        Wynik:
        28.06.2026 10:30

        Jeśli data jest null, zwracamy pusty tekst,
        żeby nie wywalić NullPointerException.
    */
    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        return dateTime.format(DISPLAY_FORMATTER);
    }

    /*
        ============================================================
        FORMATOWANIE DATY DO INPUTA HTML
        ============================================================

        Zamienia LocalDateTime na format potrzebny do:

        <input type="datetime-local">

        Przykład:

        LocalDateTime:
        2026-06-28T10:30

        Wynik:
        2026-06-28T10:30

        Ten format jest ważny, bo zwykły input datetime-local
        nie przyjmie np. "28.06.2026 10:30".
    */
    public static String formatForHtmlInput(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        return dateTime.format(HTML_DATE_TIME_FORMATTER);
    }

    /*
        ============================================================
        SPRAWDZENIE, CZY DATA JEST W PRZYSZŁOŚCI
        ============================================================

        Zwraca true, jeśli podana data jest później niż teraz.

        Przykład:

        teraz = 2026-06-28 10:00
        data  = 2026-06-29 10:00

        wynik = true

        Przydatne, jeśli chcesz zablokować rezerwacje w przeszłości.
    */
    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }

        return dateTime.isAfter(LocalDateTime.now());
    }

    /*
        ============================================================
        SPRAWDZENIE, CZY DATA JEST W PRZESZŁOŚCI
        ============================================================

        Zwraca true, jeśli podana data jest wcześniejsza niż teraz.

        Przykład:

        teraz = 2026-06-28 10:00
        data  = 2026-06-27 10:00

        wynik = true
    */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }

        return dateTime.isBefore(LocalDateTime.now());
    }

    /*
        ============================================================
        SPRAWDZENIE POPRAWNEGO ZAKRESU DAT
        ============================================================

        Sprawdza, czy:

        - start nie jest null,
        - end nie jest null,
        - end jest po start.

        Przykład poprawny:

        start = 10:00
        end   = 12:00

        Przykład błędny:

        start = 12:00
        end   = 10:00
    */
    public static boolean isCorrectRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }

        return end.isAfter(start);
    }

    /*
        ============================================================
        SPRAWDZENIE NAKŁADANIA SIĘ TERMINÓW
        ============================================================

        Ta metoda sprawdza, czy dwa przedziały czasu się nakładają.

        Przedział 1:
        existingStart - existingEnd

        Przedział 2:
        newStart - newEnd

        Konflikt jest wtedy, gdy:

        newStart < existingEnd
        oraz
        newEnd > existingStart

        Przykład konfliktu:

        istniejąca rezerwacja:
        10:00 - 12:00

        nowa rezerwacja:
        11:00 - 13:00

        Wynik:
        true

        Przykład bez konfliktu:

        istniejąca:
        10:00 - 12:00

        nowa:
        12:00 - 14:00

        Wynik:
        false

        Bo jedna kończy się dokładnie wtedy,
        gdy druga się zaczyna.
    */
    public static boolean overlaps(LocalDateTime existingStart,
                                   LocalDateTime existingEnd,
                                   LocalDateTime newStart,
                                   LocalDateTime newEnd) {
        if (existingStart == null || existingEnd == null || newStart == null || newEnd == null) {
            return false;
        }

        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }

    /*
        ============================================================
        AKTUALNA DATA DO HTML INPUT
        ============================================================

        Zwraca aktualny czas w formacie pasującym do input datetime-local.

        Przydatne w HTML-u jako wartość minimalna.

        Przykład:

        min="2026-06-28T10:30"
    */
    public static String nowForHtmlInput() {
        return formatForHtmlInput(LocalDateTime.now());
    }
}