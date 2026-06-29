package com.example.egzamin.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
    ============================================================
    PLIK ZMIENNY NR 8: AlgorithmService.java
    ============================================================

    To jest najważniejsze miejsce na ALGORYTM do punktów.

    Zasada szablonu:

    - Controller tylko odbiera dane z formularza albo REST API.
    - Service zapisuje dane i pilnuje bazy.
    - AlgorithmService wykonuje konkretną logikę zadania.

    Dzięki temu na egzaminie możesz łatwo pokazać:

    1. UI wysyła dane.
    2. Backend uruchamia algorytm.
    3. Wynik trafia do bazy albo do odpowiedzi REST.
    4. Ten sam algorytm ma test jednostkowy.

    W tym pliku zostawiłem kilka gotowych algorytmów do najczęstszych tematów:

    - wypożyczalnia samochodów / hotel: liczenie ceny i zniżki,
    - weterynarz / rezerwacje: wykrywanie kolizji terminów,
    - paczkomaty: wybór najmniejszej pasującej skrytki,
    - kino: proponowanie miejsc obok siebie.

    Przy konkretnym temacie NIE musisz używać wszystkich metod.
    Wybierasz tę, która pasuje do zadania.
*/
@Service
public class AlgorithmService {

    /*
        ============================================================
        ALGORYTM 1: LICZENIE CENY
        ============================================================

        Pasuje do tematów:
        - wypożyczalnia samochodów,
        - hotel,
        - rezerwacja sal,
        - wypożyczalnia sprzętu,
        - weterynarz, jeśli wizyta ma stałą cenę.

        Założenie:
        pricePerUnit to cena za jedną dobę / jedną usługę / jedną jednostkę.

        Liczba jednostek jest liczona jako zaokrąglenie w górę do pełnej doby.
        Czyli:
        - 2 godziny -> 1 jednostka,
        - 24 godziny -> 1 jednostka,
        - 25 godzin -> 2 jednostki.

        To jest proste i dobrze pasuje do auta/hotelu.

        Zniżki przykładowe:
        - od 7 jednostek: 10%,
        - od 14 jednostek: 15%,
        - dodatkowa zniżka użytkownika: 5%.

        Na egzaminie możesz zmienić same progi i procenty.
    */
    public double calculatePrice(Double pricePerUnit,
                                 LocalDateTime startDateTime,
                                 LocalDateTime endDateTime,
                                 boolean additionalDiscount) {
        if (pricePerUnit == null || pricePerUnit < 0) {
            throw new IllegalArgumentException("Cena jednostkowa nie może być pusta ani ujemna");
        }

        validateDateRange(startDateTime, endDateTime);

        long units = calculateRoundedUnits(startDateTime, endDateTime);
        double basePrice = pricePerUnit * units;
        double discountPercent = calculateDiscountPercent(units, additionalDiscount);
        double finalPrice = basePrice * (1.0 - discountPercent);

        return roundToTwoDecimals(finalPrice);
    }

    /*
        ============================================================
        ALGORYTM 2: KOLIZJA TERMINÓW
        ============================================================

        Pasuje do tematów:
        - weterynarz,
        - wypożyczalnia aut,
        - hotel,
        - rezerwacja sal,
        - rezerwacja sprzętu.

        Warunek konfliktu:

        nowyStart < istniejącyKoniec
        oraz
        nowyKoniec > istniejącyStart

        Przykład konfliktu:
        istniejący: 10:00-11:00
        nowy:       10:30-11:30

        Przykład bez konfliktu:
        istniejący: 10:00-11:00
        nowy:       11:00-12:00
    */
    public boolean hasTimeConflict(LocalDateTime newStart,
                                   LocalDateTime newEnd,
                                   LocalDateTime existingStart,
                                   LocalDateTime existingEnd) {
        validateDateRange(newStart, newEnd);
        validateDateRange(existingStart, existingEnd);

        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }

    /*
        ============================================================
        ALGORYTM 3: NAJMNIEJSZA PASUJĄCA SKRYTKA
        ============================================================

        Pasuje do tematu:
        - system paczkomatów.

        Założenie:
        Gabaryty zapisujemy jako liczby:

        S = 1
        M = 2
        L = 3

        Przykład:
        paczka M ma rozmiar 2.
        Wolne skrytki: 1, 2, 3.
        Algorytm wybiera 2, bo to najmniejsza pasująca skrytka.

        Zwracamy Optional<Integer>, bo może nie być żadnej pasującej skrytki.
    */
    public Optional<Integer> findSmallestLockerCapacity(Integer packageSize,
                                                        List<Integer> availableLockerCapacities) {
        if (packageSize == null || packageSize <= 0) {
            throw new IllegalArgumentException("Rozmiar paczki musi być dodatni");
        }

        if (availableLockerCapacities == null || availableLockerCapacities.isEmpty()) {
            return Optional.empty();
        }

        return availableLockerCapacities
                .stream()
                .filter(capacity -> capacity != null)
                .filter(capacity -> capacity >= packageSize)
                .min(Comparator.naturalOrder());
    }

    /*
        ============================================================
        POMOCNICZO: ZAMIANA GABARYTU NA LICZBĘ
        ============================================================

        Przy formularzu łatwiej czasem wysłać "S", "M" albo "L".
        Algorytm pracuje na liczbach, więc ta metoda robi zamianę.
    */
    public int parcelSizeToNumber(String size) {
        if (size == null) {
            throw new IllegalArgumentException("Rozmiar paczki jest wymagany");
        }

        String normalizedSize = size.trim().toUpperCase();

        return switch (normalizedSize) {
            case "S" -> 1;
            case "M" -> 2;
            case "L" -> 3;
            default -> throw new IllegalArgumentException("Nieznany rozmiar paczki: " + size);
        };
    }

    /*
        ============================================================
        ALGORYTM 4: PROPONOWANIE MIEJSC OBOK SIEBIE
        ============================================================

        Pasuje do tematu:
        - rezerwacja biletów w kinie.

        Założenie uproszczone:
        seatsInRow to miejsca w jednej kolejności, np.:
        A1, A2, A3, A4, A5

        takenSeats to miejsca już zajęte, np.:
        A2, A3

        requestedCount to liczba miejsc, np. 3.

        Algorytm znajduje pierwszy ciąg wolnych miejsc obok siebie.
        Jeżeli nie znajdzie, zwraca pustą listę.
    */
    public List<String> suggestSeatsTogether(List<String> seatsInRow,
                                             List<String> takenSeats,
                                             int requestedCount) {
        if (requestedCount <= 0) {
            throw new IllegalArgumentException("Liczba miejsc musi być dodatnia");
        }

        if (seatsInRow == null || seatsInRow.isEmpty()) {
            return List.of();
        }

        List<String> taken = takenSeats == null ? List.of() : takenSeats;
        List<String> currentBlock = new ArrayList<>();

        for (String seat : seatsInRow) {
            if (seat == null || taken.contains(seat)) {
                currentBlock.clear();
                continue;
            }

            currentBlock.add(seat);

            if (currentBlock.size() == requestedCount) {
                return new ArrayList<>(currentBlock);
            }
        }

        return List.of();
    }

    private long calculateRoundedUnits(LocalDateTime startDateTime,
                                       LocalDateTime endDateTime) {
        long minutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);
        long minutesInDay = 24 * 60;

        return Math.max(1, (minutes + minutesInDay - 1) / minutesInDay);
    }

    private double calculateDiscountPercent(long units, boolean additionalDiscount) {
        double discountPercent = 0.0;

        if (units >= 14) {
            discountPercent += 0.15;
        } else if (units >= 7) {
            discountPercent += 0.10;
        }

        if (additionalDiscount) {
            discountPercent += 0.05;
        }

        return discountPercent;
    }

    private void validateDateRange(LocalDateTime startDateTime,
                                   LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            throw new IllegalArgumentException("Początek i koniec są wymagane");
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException("Koniec musi być później niż początek");
        }
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
