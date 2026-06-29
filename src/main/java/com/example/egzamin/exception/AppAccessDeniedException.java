package com.example.egzamin.exception;

/*
    Ten wyjątek oznacza brak dostępu do jakiejś operacji.
    Przykłady:
    - użytkownik próbuje anulować cudzą rezerwację,
    - użytkownik próbuje wejść do danych, które nie należą do niego,
    - zwykły USER próbuje wykonać akcję, która powinna być tylko dla admina.

    Nie mylić z wyjątkami Spring Security.
    Spring Security blokuje dostęp do adresów, np. /admin.
    A ten wyjątek możemy rzucić sami w serwisie,
    kiedy logika aplikacji mówi: "nie wolno".
*/
public class AppAccessDeniedException extends RuntimeException {

    /*
        Konstruktor z komunikatem.

        Przykład użycia:

        throw new AppAccessDeniedException("Nie masz dostępu do tej rezerwacji");

        Dzięki temu możemy potem pokazać użytkownikowi jasny komunikat.
    */
    public AppAccessDeniedException(String message) {
        super(message);
    }

    /*
        Konstruktor z komunikatem i przyczyną.

        Rzadziej używany, ale bezpiecznie go mieć.
    */
    public AppAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}