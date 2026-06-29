package com.example.egzamin.rest;

import com.example.egzamin.dto.MainObjectDto;
import com.example.egzamin.entity.MainObject;
import com.example.egzamin.service.MainObjectService;
import com.example.egzamin.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
    ============================================================
    REST: MainRestController
    ============================================================

    Ten kontroler zwraca dane jako JSON.

    To NIE są strony HTML.
    HTML obsługują kontrolery z paczki controller.

    Ten REST jest dla zwykłych danych głównego obiektu.

    Przykłady adresów:

    GET /api/main-objects
        -> aktywne obiekty, np. dostępne sale

    GET /api/main-objects/all
        -> wszystkie obiekty

    GET /api/main-objects/1
        -> jeden konkretny obiekt

    GET /api/main-objects/1/available?startDateTime=...&endDateTime=...
        -> sprawdzenie dostępności terminu
*/
@RestController
@RequestMapping("/api/main-objects")
public class MainRestController {

    private final MainObjectService mainObjectService;
    private final ReservationService reservationService;

    public MainRestController(MainObjectService mainObjectService,
                              ReservationService reservationService) {
        this.mainObjectService = mainObjectService;
        this.reservationService = reservationService;
    }

    /*
        ============================================================
        AKTYWNE OBIEKTY
        ============================================================

        Zwraca tylko obiekty aktywne.

        Czyli np.:
        - sale dostępne do rezerwacji,
        - auta dostępne,
        - pokoje dostępne,
        - sprzęt dostępny.

        Zwracamy DTO, a nie encję, żeby JSON był prostszy.
    */
    @GetMapping
    public List<MainObjectDto> getActiveMainObjects() {
        return mainObjectService.findAllActive()
                .stream()
                .map(MainObjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        WSZYSTKIE OBIEKTY
        ============================================================

        Zwraca wszystkie obiekty, także nieaktywne.

        Przydatne bardziej dla admina albo testów.
    */
    @GetMapping("/all")
    public List<MainObjectDto> getAllMainObjects() {
        return mainObjectService.findAll()
                .stream()
                .map(MainObjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
        ============================================================
        OBIEKT PO ID
        ============================================================

        Zwraca jeden obiekt po ID.

        Przykład:
        GET /api/main-objects/3
    */
    @GetMapping("/{id}")
    public MainObjectDto getMainObjectById(@PathVariable Long id) {
        MainObject mainObject = mainObjectService.findById(id);
        return MainObjectDto.fromEntity(mainObject);
    }

    /*
        ============================================================
        SPRAWDZANIE DOSTĘPNOŚCI
        ============================================================

        Przykład zapytania:

        /api/main-objects/1/available?startDateTime=2026-06-28T10:00&endDateTime=2026-06-28T12:00

        Zwraca:
        true  -> termin wolny
        false -> termin zajęty

        Format daty jest taki jak w input datetime-local.
    */
    @GetMapping("/{id}/available")
    public boolean isAvailable(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDateTime
    ) {
        return reservationService.isAvailable(id, startDateTime, endDateTime);
    }
}