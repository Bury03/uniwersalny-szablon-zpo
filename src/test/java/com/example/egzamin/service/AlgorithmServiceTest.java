package com.example.egzamin.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
    ============================================================
    TESTY ALGORYTMU
    ============================================================

    To jest plik pod punkty za test jednostkowy.

    Testujemy AlgorithmService bez uruchamiania całej aplikacji Spring.
    Dzięki temu test jest prosty, szybki i łatwy do wytłumaczenia.

    Na egzaminie możesz zostawić tylko test pasujący do tematu,
    ale najlepiej mieć kilka przykładów, żeby szybko przerobić jeden z nich.
*/
class AlgorithmServiceTest {

    private final AlgorithmService algorithmService = new AlgorithmService();

    @Test
    void calculatePriceShouldAddLongTermDiscount() {
        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 11, 10, 0);

        double result = algorithmService.calculatePrice(100.0, start, end, false);

        assertEquals(900.0, result);
    }

    @Test
    void hasTimeConflictShouldReturnTrueForOverlappingDates() {
        LocalDateTime existingStart = LocalDateTime.of(2026, 7, 1, 10, 0);
        LocalDateTime existingEnd = LocalDateTime.of(2026, 7, 1, 11, 0);
        LocalDateTime newStart = LocalDateTime.of(2026, 7, 1, 10, 30);
        LocalDateTime newEnd = LocalDateTime.of(2026, 7, 1, 11, 30);

        boolean result = algorithmService.hasTimeConflict(
                newStart,
                newEnd,
                existingStart,
                existingEnd
        );

        assertTrue(result);
    }

    @Test
    void hasTimeConflictShouldReturnFalseWhenOneReservationEndsAndSecondStarts() {
        LocalDateTime existingStart = LocalDateTime.of(2026, 7, 1, 10, 0);
        LocalDateTime existingEnd = LocalDateTime.of(2026, 7, 1, 11, 0);
        LocalDateTime newStart = LocalDateTime.of(2026, 7, 1, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2026, 7, 1, 12, 0);

        boolean result = algorithmService.hasTimeConflict(
                newStart,
                newEnd,
                existingStart,
                existingEnd
        );

        assertFalse(result);
    }

    @Test
    void findSmallestLockerCapacityShouldChooseSmallestPossibleLocker() {
        Optional<Integer> result = algorithmService.findSmallestLockerCapacity(
                2,
                List.of(3, 1, 2)
        );

        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    void suggestSeatsTogetherShouldReturnFirstAvailableBlock() {
        List<String> result = algorithmService.suggestSeatsTogether(
                List.of("A1", "A2", "A3", "A4", "A5"),
                List.of("A1", "A2"),
                3
        );

        assertEquals(List.of("A3", "A4", "A5"), result);
    }
}
