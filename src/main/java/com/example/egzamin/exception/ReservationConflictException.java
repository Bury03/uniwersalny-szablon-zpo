package com.example.egzamin.exception;

/*
    Ten wyjątek oznacza konflikt rezerwacji.

    Czyli sytuację, gdy ktoś próbuje zarezerwować obiekt
    w terminie, który jest już zajęty.

    Przykład:

    Istniejąca rezerwacja:
    10:00 - 12:00

    Nowa rezerwacja:
    11:00 - 13:00

    To jest konflikt, więc ReservationService rzuca ten wyjątek.

    Ten wyjątek pasuje do każdego tematu:

    - sale,
    - auta,
    - pokoje,
    - sprzęt,
    - gabinety,
    - usługi.
*/
public class ReservationConflictException extends RuntimeException {

    /*
        Konstruktor z komunikatem.

        Przykład użycia:

        throw new ReservationConflictException("Wybrany termin jest już zajęty");

        Dzięki temu w formularzu można pokazać użytkownikowi
        konkretny powód błędu.
    */
    public ReservationConflictException(String message) {
        super(message);
    }

    /*
        Konstruktor z komunikatem i przyczyną.

        Nie zawsze będzie potrzebny,
        ale nie przeszkadza i zwiększa elastyczność klasy.
    */
    public ReservationConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}