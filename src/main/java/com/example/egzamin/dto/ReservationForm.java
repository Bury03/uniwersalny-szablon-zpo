package com.example.egzamin.dto;

import com.example.egzamin.entity.Reservation;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/*
    ============================================================
    PLIK ZMIENNY NR 5: ReservationForm.java
    ============================================================

    Ta klasa NIE jest encją bazodanową.

    Czyli:
    - NIE ma tutaj @Entity,
    - NIE ma tutaj @Table,
    - NIE tworzy osobnej tabeli w bazie danych.

    To jest DTO / formularz pomocniczy.

    Najprościej:

    Reservation.java     -> rezerwacja zapisana w bazie danych
    ReservationForm.java -> dane wpisane przez użytkownika w formularzu

    Przykład:

    Użytkownik w przeglądarce wybiera:
    - obiekt: Sala 101,
    - początek: 2026-06-28 10:00,
    - koniec: 2026-06-28 12:00,
    - notatka: "Potrzebny projektor".

    Te dane trafiają najpierw do ReservationForm.
    Potem ReservationService zamienia to na Reservation.
*/
public class ReservationForm {

    /*
        ============================================================
        ID
        ============================================================

        ID formularza / rezerwacji.

        Przy dodawaniu nowej rezerwacji:
        - id najczęściej jest null.

        Przy edycji istniejącej rezerwacji:
        - id mówi, którą rezerwację edytujemy.

        Nawet jeśli teraz nie robimy edycji rezerwacji,
        to pole warto zostawić, bo nie przeszkadza.
    */
    private Long id;

    /*
        ============================================================
        MAIN OBJECT ID
        ============================================================

        To jest ID obiektu, który użytkownik chce zarezerwować.

        Nie trzymamy tutaj całego MainObject,
        tylko jego ID.

        Dlaczego?

        Bo formularz HTML najczęściej przesyła coś takiego:

        <select name="mainObjectId">
            <option value="1">Sala 101</option>
            <option value="2">Sala 102</option>
        </select>

        Czyli z formularza przychodzi np.:

        mainObjectId = 1

        Potem ReservationService po tym ID znajduje prawdziwy obiekt
        w bazie danych.
    */
    private Long mainObjectId;

    /*
        ============================================================
        START DATE TIME
        ============================================================

        Data i godzina rozpoczęcia rezerwacji.

        Używamy LocalDateTime, czyli daty razem z godziną.

        Przykład:
        2026-06-28 10:00

        Adnotacja @DateTimeFormat jest bardzo ważna przy formularzu HTML.

        Formularz z input type="datetime-local" wysyła datę w formacie:

        2026-06-28T10:00

        Dlatego ustawiamy pattern:

        yyyy-MM-dd'T'HH:mm

        Ten znak T jest normalny w HTML-u.
        Nie jest błędem.
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    /*
        ============================================================
        END DATE TIME
        ============================================================

        Data i godzina zakończenia rezerwacji.

        Tak samo jak wyżej, używamy @DateTimeFormat,
        żeby Spring dobrze odczytał wartość z formularza HTML.

        Ważna zasada logiczna:

        endDateTime musi być późniejsze niż startDateTime.

        Ale tego nie sprawdzamy tutaj.
        Tym zajmuje się ReservationService.

        Dlaczego?
        Bo formularz ma tylko przenosić dane,
        a logika biznesowa powinna być w serwisie.
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    /*
        ============================================================
        NOTE
        ============================================================

        Dodatkowa notatka od użytkownika.

        Przykłady:

        - dla sali: "Potrzebny projektor"
        - dla auta: "Odbiór rano"
        - dla pokoju: "Poproszę pokój z balkonem"
        - dla sprzętu: "Sprzęt potrzebny na zajęcia"

        To pole może być puste.
        Nie każda rezerwacja musi mieć notatkę.
    */
    private String note;

    /*
        ============================================================
        EXTRA DATA
        ============================================================

        Dodatkowe dane zależne od tematu.

        Przykłady:
        - kino: miejsca, np. A1,A2,A3,
        - paczkomat: gabaryt paczki albo telefon odbiorcy,
        - weterynarz: imię zwierzęcia,
        - auto: informacja o kierowcy.
    */
    private String extraData;

    /*
        ============================================================
        DISCOUNT
        ============================================================

        Prosty checkbox / parametr do algorytmu ceny.

        Dla wypożyczalni aut może oznaczać np. zniżkę dla stałego klienta.
        Dla innych tematów możesz tego pola nie używać.
    */
    private boolean discount;

    /*
        ============================================================
        KONSTRUKTOR PUSTY
        ============================================================

        Spring potrzebuje pustego konstruktora,
        żeby stworzyć obiekt formularza i wypełnić go danymi.

        Tego konstruktora nie usuwamy.
    */
    public ReservationForm() {
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY
        ============================================================

        Ten konstruktor jest dla wygody.

        Może się przydać np. w testach,
        albo kiedy ręcznie tworzymy formularz w kodzie.
    */
    public ReservationForm(Long id,
                           Long mainObjectId,
                           LocalDateTime startDateTime,
                           LocalDateTime endDateTime,
                           String note) {
        this.id = id;
        this.mainObjectId = mainObjectId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.note = note;
    }

    public ReservationForm(Long id,
                           Long mainObjectId,
                           LocalDateTime startDateTime,
                           LocalDateTime endDateTime,
                           String note,
                           String extraData,
                           boolean discount) {
        this.id = id;
        this.mainObjectId = mainObjectId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.note = note;
        this.extraData = extraData;
        this.discount = discount;
    }

    /*
        ============================================================
        METODA fromReservation()
        ============================================================

        Ta metoda zamienia encję Reservation na formularz ReservationForm.

        Czyli:

        Reservation -> ReservationForm

        Przydaje się np. przy edycji rezerwacji.

        Mamy rezerwację w bazie, pobieramy ją,
        a potem chcemy wypełnić formularz jej aktualnymi danymi.
    */
    public static ReservationForm fromReservation(Reservation reservation) {
        /*
            Jeżeli reservation jest null,
            to zwracamy pusty formularz.

            Dzięki temu nie wywali się NullPointerException.
        */
        if (reservation == null) {
            return new ReservationForm();
        }

        ReservationForm form = new ReservationForm();

        form.setId(reservation.getId());
        form.setStartDateTime(reservation.getStartDateTime());
        form.setEndDateTime(reservation.getEndDateTime());
        form.setNote(reservation.getNote());
        form.setExtraData(reservation.getExtraData());

        /*
            MainObject może być teoretycznie null,
            więc sprawdzamy to przed pobraniem ID.
        */
        if (reservation.getMainObject() != null) {
            form.setMainObjectId(reservation.getMainObject().getId());
        }

        return form;
    }

    /*
        ============================================================
        METODY POMOCNICZE
        ============================================================

        Te metody nie są obowiązkowe,
        ale ułatwiają sprawdzanie formularza w serwisie.

        Nie wrzucamy tutaj ciężkiej logiki.
        Tylko proste pomocnicze sprawdzenia.
    */

    public boolean hasMainObjectSelected() {
        /*
            Zwraca true, jeśli użytkownik wybrał jakiś obiekt.

            Czyli mainObjectId nie jest null.
        */
        return mainObjectId != null;
    }

    public boolean hasStartAndEndDateTime() {
        /*
            Zwraca true, jeśli użytkownik podał obie daty.

            Czyli mamy:
            - początek rezerwacji,
            - koniec rezerwacji.
        */
        return startDateTime != null && endDateTime != null;
    }

    public boolean hasCorrectDateOrder() {
        /*
            Sprawdza, czy koniec jest po początku.

            Przykład poprawny:
            start = 10:00
            end   = 12:00

            Przykład błędny:
            start = 12:00
            end   = 10:00

            Jeżeli daty są puste, zwracamy false.
        */
        if (!hasStartAndEndDateTime()) {
            return false;
        }

        return endDateTime.isAfter(startDateTime);
    }

    /*
        ============================================================
        GETTERY I SETTERY
        ============================================================

        Są potrzebne, żeby:

        - Thymeleaf mógł odczytać dane z formularza,
        - Spring mógł wpisać dane z formularza do obiektu,
        - ReservationService mógł użyć tych danych,
        - testy mogły łatwo tworzyć przykładowe formularze.
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        /*
            Ustawia ID rezerwacji.

            Przy nowej rezerwacji najczęściej będzie null.
        */
        this.id = id;
    }

    public Long getMainObjectId() {
        return mainObjectId;
    }

    public void setMainObjectId(Long mainObjectId) {
        /*
            Ustawia ID obiektu, który ma zostać zarezerwowany.

            Przykład:
            form.setMainObjectId(1L);
        */
        this.mainObjectId = mainObjectId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        /*
            Ustawia początek rezerwacji.
        */
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        /*
            Ustawia koniec rezerwacji.
        */
        this.endDateTime = endDateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        /*
            Ustawia notatkę użytkownika.

            Pole może być puste.
        */
        this.note = note;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        /*
            Ustawia dane dodatkowe zależne od tematu.
        */
        this.extraData = extraData;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        /*
            Ustawia informację, czy ma zostać użyta dodatkowa zniżka.
        */
        this.discount = discount;
    }

}