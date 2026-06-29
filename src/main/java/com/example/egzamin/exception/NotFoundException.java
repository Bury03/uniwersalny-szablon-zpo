package com.example.egzamin.exception;

/*
    Ten wyjątek oznacza sytuację:
    "Nie znaleziono czegoś w systemie".
    Przykłady:
    - nie znaleziono użytkownika,
    - nie znaleziono rezerwacji,
    - nie znaleziono obiektu głównego,
    - nie znaleziono kategorii / typu.

    Dzięki temu zamiast zwracać null i potem mieć dziwne błędy,
    od razu rzucamy jasny wyjątek z komunikatem.
*/
public class NotFoundException extends RuntimeException {

    /*
        Konstruktor z komunikatem.

        Przykład użycia:

        throw new NotFoundException("Nie znaleziono obiektu o ID: 5");

        Ten tekst potem może być pokazany użytkownikowi
        albo zapisany w logach.
    */
    public NotFoundException(String message) {
        super(message);
    }

    /*
        Konstruktor z komunikatem i przyczyną.

        Rzadziej używany, ale przydatny,
        jeśli jeden wyjątek wynika z innego wyjątku.
    */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}