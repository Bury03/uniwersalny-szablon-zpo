package com.example.egzamin.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
    ============================================================
    PLIK ZMIENNY NR 1: MainObject.java
    ============================================================

    Ta klasa reprezentuje GŁÓWNY OBIEKT w aplikacji.

    Czyli w zależności od treści zadania na egzaminie może to być np.:

    - sala,
    - pokój hotelowy,
    - samochód,
    - sprzęt,
    - książka,
    - gabinet,
    - usługa,
    - boisko,
    - stolik w restauracji,
    - cokolwiek, co da się wyświetlać, edytować albo rezerwować.

    WAŻNE:
    Nazwy klasy "MainObject" NIE zmieniamy, bo inne pliki w projekcie
    już z niej korzystają, np.:

    - MainObjectRepository,
    - MainObjectService,
    - MainObjectController,
    - MainRestController,
    - Reservation,
    - ReservationService.

    Dzięki temu na egzaminie zmieniasz głównie pola i teksty,
    a nie całą strukturę projektu.
*/

@Entity
@Table(name = "main_objects")
public class MainObject {

    /*
        ============================================================
        ID
        ============================================================

        Każdy obiekt w bazie musi mieć swoje unikalne ID.

        Przykład:
        - sala nr 1 ma id = 1,
        - sala nr 2 ma id = 2,
        - samochód nr 1 ma id = 1 itd.

        Tego pola zazwyczaj NIE zmieniamy.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        ============================================================
        NAME
        ============================================================

        Nazwa głównego obiektu.

        Przykłady w różnych zadaniach:

        - dla sali: "Sala 101"
        - dla samochodu: "Toyota Corolla"
        - dla pokoju: "Pokój 2-osobowy"
        - dla sprzętu: "Projektor Epson"
        - dla książki: "Programowanie w Javie"

        To pole jest bardzo uniwersalne, dlatego zostawiamy nazwę "name".
        W HTML-u można później wyświetlać to jako "Nazwa", "Tytuł",
        "Model", "Sala", zależnie od tematu.
    */
    @Column(length = 100)
    private String name;

    /*
        ============================================================
        DESCRIPTION
        ============================================================

        Krótki opis obiektu.

        Przykłady:

        - sala: "Sala komputerowa z 20 stanowiskami"
        - auto: "Samochód osobowy z klimatyzacją"
        - sprzęt: "Projektor dostępny dla prowadzących"
        - pokój: "Pokój z łazienką i balkonem"

        To pole też jest uniwersalne i bardzo często przydaje się
        w każdym temacie projektu.
    */
    @Column(length = 1000)
    private String description;

    /*
        ============================================================
        LOCATION
        ============================================================

        Lokalizacja / miejsce / adres / numer pomieszczenia.

        Przykłady:

        - sala: "Budynek A, piętro 2"
        - pokój: "Hotel główny, piętro 1"
        - samochód: "Parking główny"
        - sprzęt: "Magazyn IT"
        - książka: "Regał B3"

        Jeśli w danym zadaniu lokalizacja nie ma sensu,
        można to pole zostawić i po prostu użyć jako dodatkową informację.
    */
    @Column(length = 150)
    private String location;

    /*
        ============================================================
        CAPACITY
        ============================================================

        Pojemność / liczba miejsc / ilość osób / dostępna liczba.

        Przykłady:

        - sala: liczba miejsc w sali,
        - pokój: liczba osób,
        - samochód: liczba miejsc,
        - sprzęt: ilość dostępnych sztuk,
        - wydarzenie: limit uczestników.

        Jeżeli temat nie potrzebuje pojemności, nadal można to pole zostawić,
        bo nie przeszkadza w działaniu aplikacji.
    */
    private Integer capacity;

    /*
        ============================================================
        PRICE
        ============================================================

        Cena / koszt / stawka.

        Przykłady:

        - cena za godzinę rezerwacji sali,
        - cena za dobę pokoju,
        - koszt wypożyczenia samochodu,
        - opłata za usługę.

        Używam Double, bo jest proste na egzamin.
        W profesjonalnych projektach do pieniędzy częściej daje się BigDecimal,
        ale tutaj zależy nam na prostocie i szybkim działaniu.
    */
    private Double price;

    /*
        ============================================================
        ACTIVE
        ============================================================

        Informacja, czy obiekt jest aktywny.

        true  -> obiekt jest widoczny i można go używać,
        false -> obiekt jest wyłączony, np. sala jest w remoncie.

        Przykłady:

        - sala aktywna,
        - samochód niedostępny,
        - sprzęt uszkodzony,
        - pokój wyłączony z rezerwacji.

        To pole bardzo się przydaje, bo zamiast usuwać obiekt z bazy,
        można go tylko dezaktywować.
    */
    private boolean active = true;

    /*
        ============================================================
        RELACJA DO AdditionalEntity
        ============================================================

        AdditionalEntity to encja pomocnicza.

        W zależności od zadania może oznaczać np.:

        - kategorię sali,
        - typ samochodu,
        - kategorię książki,
        - rodzaj sprzętu,
        - dział,
        - lokalizację,
        - grupę obiektów.

        Relacja ManyToOne oznacza:

        Wiele MainObject może należeć do jednej AdditionalEntity.

        Przykład:
        - wiele sal może należeć do kategorii "Laboratorium",
        - wiele aut może należeć do typu "Osobowe",
        - wiele książek może należeć do kategorii "Informatyka".
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_entity_id")
    private AdditionalEntity additionalEntity;

    /*
        ============================================================
        RELACJA DO Reservation
        ============================================================

        Jeden MainObject może mieć wiele rezerwacji.

        Przykład:

        Sala 101:
        - rezerwacja 1: poniedziałek 10:00-12:00,
        - rezerwacja 2: wtorek 14:00-16:00,
        - rezerwacja 3: piątek 08:00-09:00.

        mappedBy = "mainObject" oznacza, że w klasie Reservation
        znajduje się pole o nazwie "mainObject".

        Czyli Reservation jest właścicielem relacji,
        a MainObject tylko pokazuje listę swoich rezerwacji.
    */
    @OneToMany(
            mappedBy = "mainObject",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    /*
        ============================================================
        KONSTRUKTOR BEZARGUMENTOWY
        ============================================================

        Hibernate / JPA wymaga pustego konstruktora.

        Bez tego aplikacja może się wywalić przy starcie,
        bo Spring nie będzie umiał tworzyć obiektów tej klasy z bazy danych.
    */
    public MainObject() {
    }

    /*
        ============================================================
        KONSTRUKTOR POMOCNICZY
        ============================================================

        Ten konstruktor jest dla naszej wygody.

        Można go używać np. w DataInitializer,
        żeby szybko stworzyć przykładowe obiekty startowe.

        Nie dajemy tutaj pola id, bo id tworzy się samo w bazie danych.
    */
    public MainObject(String name,
                      String description,
                      String location,
                      Integer capacity,
                      Double price,
                      boolean active,
                      AdditionalEntity additionalEntity) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.price = price;
        this.active = active;
        this.additionalEntity = additionalEntity;
    }

    /*
        ============================================================
        GETTERY I SETTERY
        ============================================================

        Gettery i settery są potrzebne, żeby:

        - Thymeleaf mógł odczytywać dane w HTML,
        - formularze mogły wpisywać dane do obiektu,
        - serwisy mogły zmieniać dane,
        - Spring mógł poprawnie mapować dane z bazy.

        Przykład:
        getName() pozwala pobrać nazwę,
        setName(...) pozwala zmienić nazwę.
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        /*
            ID zazwyczaj ustawia baza danych automatycznie.
            Setter jednak zostawiamy, bo bywa potrzebny np. w testach.
        */
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        /*
            Tutaj ustawiamy nazwę obiektu.

            Przykład:
            mainObject.setName("Sala 101");
        */
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        /*
            Tutaj ustawiamy opis obiektu.

            Przykład:
            mainObject.setDescription("Sala komputerowa z projektorem");
        */
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        /*
            Tutaj ustawiamy lokalizację.

            Przykład:
            mainObject.setLocation("Budynek A, piętro 2");
        */
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        /*
            Tutaj ustawiamy pojemność.

            Przykład:
            mainObject.setCapacity(30);
        */
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        /*
            Tutaj ustawiamy cenę.

            Przykład:
            mainObject.setPrice(50.0);
        */
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        /*
            Tutaj ustawiamy, czy obiekt jest aktywny.

            true  -> aktywny,
            false -> nieaktywny.
        */
        this.active = active;
    }

    public AdditionalEntity getAdditionalEntity() {
        return additionalEntity;
    }

    public void setAdditionalEntity(AdditionalEntity additionalEntity) {
        /*
            Tutaj przypisujemy obiekt pomocniczy.

            Przykład:
            sala może mieć kategorię "Laboratorium",
            samochód może mieć typ "Osobowy",
            książka może mieć kategorię "Informatyka".
        */
        this.additionalEntity = additionalEntity;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        /*
            Tutaj ustawiamy listę rezerwacji danego obiektu.

            Normalnie rzadko ustawiamy całą listę ręcznie,
            ale setter zostawiamy dla Springa, Hibernate i testów.
        */
        this.reservations = reservations;
    }
}