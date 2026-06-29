package com.example.egzamin.repository;

import com.example.egzamin.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    ============================================================
    PLIK TECHNICZNY STAŁY: ReservationRepository.java
    ============================================================

    To repozytorium odpowiada za komunikację z tabelą reservations.

    Encja Reservation oznacza rezerwację.

    Przykłady zależnie od tematu:

    - rezerwacja sali,
    - rezerwacja samochodu,
    - rezerwacja pokoju,
    - rezerwacja sprzętu,
    - rezerwacja gabinetu,
    - rezerwacja usługi.

    Ale repozytorium NIE musi wiedzieć, czego dokładnie dotyczy rezerwacja.

    Ono tylko daje Springowi dostęp do bazy danych.
*/

/*
    @Repository mówi Springowi:

    "To jest klasa odpowiedzialna za dostęp do danych".

    Dzięki temu Spring może ją automatycznie wstrzyknąć do serwisu,
    np. do ReservationService.
*/
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /*
        ============================================================
        DLACZEGO TUTAJ NIC NIE DOPISUJEMY?
        ============================================================

        Ponieważ JpaRepository daje nam gotowe metody.

        Automatycznie mamy:

        findAll()
            -> pobiera wszystkie rezerwacje.

        findById(Long id)
            -> szuka rezerwacji po ID.

        save(Reservation reservation)
            -> zapisuje nową rezerwację albo aktualizuje istniejącą.

        delete(Reservation reservation)
            -> usuwa rezerwację.

        count()
            -> liczy rezerwacje.

        existsById(Long id)
            -> sprawdza, czy rezerwacja istnieje.

        I właśnie tych metod używa ReservationService.
    */

    /*
        ============================================================
        CZY NA EGZAMINIE COŚ TU ZMIENIASZ?
        ============================================================

        Normalnie NIE.

        Ten plik zostaje taki sam niezależnie od tego, czy projekt jest o:

        - salach,
        - autach,
        - hotelu,
        - sprzęcie,
        - bibliotece,
        - gabinetach.

        Zmieniasz dane, HTML-e i ewentualnie logikę w serwisie,
        ale repozytorium może zostać puste.
    */

    /*
        ============================================================
        KIEDY MOŻNA BY COŚ DOPISAĆ?
        ============================================================

        Tylko jeśli chcesz mieć specjalne zapytania, np.:

        List<Reservation> findByStatus(String status);

        albo:

        List<Reservation> findByUserId(Long userId);

        Ale my teraz tego NIE robimy, bo ReservationService filtruje dane
        prostą metodą findAll(), więc projekt jest mniej zależny od nazw
        metod w repozytorium.

        Na egzaminie prostsze = bezpieczniejsze.
    */
}