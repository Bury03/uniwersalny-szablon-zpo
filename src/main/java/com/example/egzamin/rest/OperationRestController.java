package com.example.egzamin.rest;

import com.example.egzamin.dto.OperationRequest;
import com.example.egzamin.dto.OperationResponse;
import com.example.egzamin.entity.Reservation;
import com.example.egzamin.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    ============================================================
    REST: OperationRestController
    ============================================================

    Ten kontroler jest przygotowany specjalnie pod punkty za REST API.

    Najczęstsze wymaganie na zaliczeniu:
    - endpoint POST do wykonania głównej operacji.

    Przykłady zależnie od tematu:

    - wypożyczalnia samochodów: POST /api/operations tworzy rezerwację auta,
    - weterynarz: POST /api/operations tworzy wizytę,
    - paczkomat: POST /api/operations nadaje paczkę,
    - kino: POST /api/operations rezerwuje miejsca.

    Nazwę ścieżki możesz zmienić pod temat, np.:
    /api/reservations, /api/parcels, /api/visits.

    Wersja szablonowa zostawia /api/operations, żeby była uniwersalna.
*/
@RestController
@RequestMapping("/api/operations")
public class OperationRestController {

    private final ReservationService reservationService;

    public OperationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /*
        ============================================================
        POST /api/operations
        ============================================================

        Tworzy nową operację dla aktualnie zalogowanego użytkownika.

        Spring Security daje nam obiekt Authentication.
        Z niego bierzemy username, więc zwykły użytkownik nie może w JSON-ie
        podszyć się pod innego użytkownika.
    */
    @PostMapping
    public OperationResponse createOperation(@RequestBody OperationRequest request,
                                             Authentication authentication) {
        Reservation reservation = reservationService.createReservation(
                request.toReservationForm(),
                authentication.getName()
        );

        return OperationResponse.fromReservation(reservation);
    }
}
