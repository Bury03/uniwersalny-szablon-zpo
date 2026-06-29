package com.example.egzamin.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
    ============================================================
    PLIK ZMIENNY NR 3: Reservation.java
    ============================================================

    Ta klasa reprezentuje REZERWACJĘ.

    Reservation łączy:

    1. użytkownika,
    2. główny obiekt aplikacji,
    3. czas rozpoczęcia,
    4. czas zakończenia.

    Czyli najprościej:

    "Kto, co i kiedy zarezerwował?"

    Przykłady zależnie od tematu:

    1. Rezerwacja sal:
       - użytkownik rezerwuje salę na konkretny termin.

    2. Wypożyczalnia samochodów:
       - użytkownik rezerwuje samochód od konkretnej daty do konkretnej daty.

    3. Hotel:
       - użytkownik rezerwuje pokój od dnia przyjazdu do dnia wyjazdu.

    4. Sprzęt:
       - użytkownik rezerwuje sprzęt na dany czas.

    5. Gabinet / usługi:
       - użytkownik rezerwuje termin wizyty.

    WAŻNE:
    Nazwy klasy "Reservation" NIE zmieniamy.
    Inne pliki projektu już zakładają, że rezerwacje są w tej klasie.
*/

@Entity
@Table(name = "reservations")
public class Reservation {

    /*
        ============================================================
        ID
        ============================================================

        Każda rezerwacja musi mieć swoje unikalne ID.

        Przykład:
        - rezerwacja nr 1,
        - rezerwacja nr 2,
        - rezerwacja nr 3.

        ID jest generowane automatycznie przez bazę danych.
        Tego pola normalnie nie zmieniamy.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        ============================================================
        USER
        ============================================================

        Użytkownik, który zrobił rezerwację.

        Relacja ManyToOne oznacza:

        Wiele rezerwacji może należeć do jednego użytkownika.

        Przykład:

        Użytkownik "jan" może mieć:
        - rezerwację sali 101,
        - rezerwację sali 202,
        - rezerwację projektora,
        - rezerwację samochodu.

        Czyli:
        jeden User -> wiele Reservation.

        FetchType.LAZY oznacza, że dane użytkownika są pobierane dopiero wtedy,
        gdy naprawdę będą potrzebne.

        To jest dobre, bo aplikacja nie ładuje od razu za dużo danych.
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*
        ============================================================
        MAIN OBJECT
        ============================================================

        Obiekt, który został zarezerwowany.

        MainObject to nasz główny obiekt aplikacji.

        W zależności od tematu może to być:

        - sala,
        - pokój,
        - samochód,
        - sprzęt,
        - książka,
        - gabinet,
        - usługa.

        Relacja ManyToOne oznacza:

        Wiele rezerwacji może dotyczyć jednego obiektu.

        Przykład:

        Sala 101 może mieć:
        - rezerwację w poniedziałek 10:00-12:00,
        - rezerwację we wtorek 14:00-16:00,
        - rezerwację w piątek 08:00-09:00.

        Czyli:
        jeden MainObject -> wiele Reservation.
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_object_id")
    private MainObject mainObject;

    /*
        ============================================================
        START DATE TIME
        ============================================================

        Data i godzina rozpoczęcia rezerwacji.

        Przykłady:

        - sala: od kiedy sala jest zajęta,
        - auto: od kiedy zaczyna się wypożyczenie,
        - hotel: od kiedy zaczyna się pobyt,
        - sprzęt: od kiedy sprzęt jest wypożyczony.

        Typ LocalDateTime przechowuje jednocześnie datę i godzinę.

        Przykład:
        2026-06-28 10:00
    */
    private LocalDateTime startDateTime;

    /*
        ============================================================
        END DATE TIME
        ============================================================

        Data i godzina zakończenia rezerwacji.

        Przykłady:

        - sala: do kiedy sala jest zajęta,
        - auto: do kiedy trwa wypożyczenie,
        - hotel: do kiedy trwa pobyt,
        - sprzęt: do kiedy sprzęt ma być oddany.

        Ważna zasada:
        endDateTime powinno być późniejsze niż startDateTime.

        Tym będzie się zajmował ReservationService,
        czyli logika aplikacji.
    */
    private LocalDateTime endDateTime;

    /*
        ============================================================
        STATUS
        ============================================================

        Status rezerwacji.

        Trzymamy go jako String, żeby było prosto na egzaminie.

        Przykładowe statusy:

        - "ACTIVE"    -> rezerwacja aktywna,
        - "CANCELLED" -> rezerwacja anulowana,
        - "FINISHED"  -> rezerwacja zakończona.

        Na start ustawiamy "ACTIVE",
        bo nowa rezerwacja normalnie od razu jest aktywna.

        Nie robimy tutaj osobnego enuma,
        bo prosta wersja jest szybsza i łatwiejsza do ogarnięcia.
    */
    @Column(length = 30)
    private String status = "ACTIVE";

    /*
        ============================================================
        NOTE
        ============================================================

        Dodatkowa notatka do rezerwacji.

        Przykłady:

        - "Potrzebny projektor"
        - "Odbiór auta rano"
        - "Pokój z widokiem"
        - "Rezerwacja dla grupy 10 osób"

        To pole nie musi być obowiązkowe.
        Może być puste.

        Jest przydatne, bo użytkownik może dopisać dodatkową informację.
    */
    @Column(length = 1000)
    private String note;

    /*
        ============================================================
        TOTAL PRICE
        ============================================================

        Wyliczona cena całkowita operacji.

        To pole bardzo pomaga przy tematach takich jak:

        - wypożyczalnia samochodów,
        - hotel,
        - kino,
        - weterynarz,
        - wypożyczalnia sprzętu.

        Najczęściej ustawiamy je w ReservationService na podstawie wyniku
        z AlgorithmService. Dzięki temu można łatwo pokazać prowadzącemu,
        że algorytm działa na backendzie i jego wynik zapisuje się w bazie.
    */
    private Double totalPrice;

    /*
        ============================================================
        EXTRA DATA
        ============================================================

        Dodatkowe dane specyficzne dla tematu.

        To pole jest celowo ogólne, żeby nie trzeba było za każdym razem
        dodawać nowej encji albo nowej kolumny.

        Przykłady użycia:

        - kino: wybrane miejsca, np. "A1,A2,A3",
        - paczkomat: gabaryt paczki albo numer odbiorcy,
        - auto: informacja o zniżce albo wieku kierowcy,
        - weterynarz: imię zwierzęcia albo krótki opis problemu.

        W dużym projekcie zrobiłoby się osobne pola i osobne encje.
        W szablonie egzaminacyjnym to pole daje prostą elastyczność.
    */
    @Column(length = 1000)
    private String extraData;

    /*
        ============================================================
        CREATED AT
        ============================================================

        Data utworzenia rezerwacji.

        Czyli kiedy użytkownik kliknął / zapisał rezerwację.

        To nie jest termin rezerwacji.
        To jest moment dodania rekordu do bazy.

        Przykład:

        Użytkownik może utworzyć rezerwację dzisiaj,
        ale sama rezerwacja może dotyczyć przyszłego tygodnia.

        createdAt pomaga potem sprawdzić,
        kiedy rezerwacja została faktycznie zrobiona.
    */
    private LocalDateTime createdAt;

    /*
        ============================================================
        KONSTRUKTOR PUSTY
        ============================================================

        Hibernate / JPA wymaga pustego konstruktora.

        Bez niego Spring może mieć problem z tworzeniem obiektów
        pobieranych z bazy danych.

        Tego konstruktora nie usuwamy.
    */
    public Reservation() {
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY
        ============================================================

        Ten konstruktor jest dla naszej wygody.

        Możemy go użyć np. w testach albo w DataInitializer,
        jeżeli chcemy szybko utworzyć przykładową rezerwację.

        Nie podajemy tutaj ID,
        bo ID nadaje baza danych automatycznie.
    */
    public Reservation(User user,
                       MainObject mainObject,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       String status,
                       String note) {
        this.user = user;
        this.mainObject = mainObject;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.note = note;
    }

    public Reservation(User user,
                       MainObject mainObject,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       String status,
                       String note,
                       Double totalPrice,
                       String extraData) {
        this.user = user;
        this.mainObject = mainObject;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.note = note;
        this.totalPrice = totalPrice;
        this.extraData = extraData;
    }

    /*
        ============================================================
        PRE PERSIST
        ============================================================

        Ta metoda uruchamia się automatycznie przed pierwszym zapisem
        rezerwacji do bazy danych.

        Dzięki temu nie musimy ręcznie ustawiać createdAt.

        Jeśli createdAt jest puste, ustawiamy aktualny czas.

        Przykład:
        użytkownik zapisuje rezerwację -> Spring zapisuje do bazy ->
        przed zapisem odpala się ta metoda -> createdAt dostaje aktualną datę.
    */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
    }

    /*
        ============================================================
        METODY POMOCNICZE
        ============================================================

        Te metody nie są obowiązkowe,
        ale bardzo pomagają w kodzie i w HTML.

        Dzięki nim możemy łatwo sprawdzić status rezerwacji.

        Przykład:
        reservation.isActive()
        zamiast:
        "ACTIVE".equals(reservation.getStatus())
    */

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    public boolean isFinished() {
        return "FINISHED".equalsIgnoreCase(status);
    }

    /*
        ============================================================
        GETTERY I SETTERY
        ============================================================

        Gettery i settery są potrzebne, żeby:

        - Spring mógł zapisywać i odczytywać dane,
        - Thymeleaf mógł pokazywać dane w HTML,
        - formularze mogły przekazywać dane do obiektów,
        - serwisy mogły zmieniać rezerwacje,
        - testy mogły łatwo ustawiać wartości.
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        /*
            ID normalnie nadaje baza danych.
            Setter zostaje głównie dla Springa i testów.
        */
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        /*
            Ustawia użytkownika, który zrobił rezerwację.

            Przykład:
            reservation.setUser(loggedUser);
        */
        this.user = user;
    }

    public MainObject getMainObject() {
        return mainObject;
    }

    public void setMainObject(MainObject mainObject) {
        /*
            Ustawia obiekt, który jest rezerwowany.

            Przykład:
            reservation.setMainObject(sala101);
        */
        this.mainObject = mainObject;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        /*
            Ustawia początek rezerwacji.

            Przykład:
            reservation.setStartDateTime(LocalDateTime.of(2026, 6, 28, 10, 0));
        */
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        /*
            Ustawia koniec rezerwacji.

            Przykład:
            reservation.setEndDateTime(LocalDateTime.of(2026, 6, 28, 12, 0));
        */
        this.endDateTime = endDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        /*
            Ustawia status rezerwacji.

            Najczęściej używane wartości:

            ACTIVE
            CANCELLED
            FINISHED

            Na egzaminie raczej nie ma sensu robić tego bardziej skomplikowanie.
        */
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        /*
            Ustawia dodatkową notatkę do rezerwacji.

            To pole może być puste.
        */
        this.note = note;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        /*
            Ustawia cenę całkowitą wyliczoną przez algorytm.
        */
        this.totalPrice = totalPrice;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        /*
            Ustawia dodatkowe dane zależne od tematu zadania.
        */
        this.extraData = extraData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        /*
            Ustawia datę utworzenia rezerwacji.

            Normalnie robi to automatycznie metoda prePersist(),
            ale setter zostaje dla Springa i testów.
        */
        this.createdAt = createdAt;
    }
}